package com.example.DealerFlow.Security.RateLimiting;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Cache<String, Bucket> publicBuckets;
    private final Cache<String, Bucket> authenticatedBuckets;
    private final RateLimitProperties properties;

    public RateLimitingFilter(RateLimitProperties properties) {
        this.properties = properties;
        this.publicBuckets = Caffeine.newBuilder()
                .maximumSize(properties.getCache().getMaxSize())
                .expireAfterAccess(properties.getCache().getExpireMinutes(), TimeUnit.MINUTES)
                .build();
        this.authenticatedBuckets = Caffeine.newBuilder()
                .maximumSize(properties.getCache().getMaxSize())
                .expireAfterAccess(properties.getCache().getExpireMinutes(), TimeUnit.MINUTES)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String key;
        Bucket bucket;

        if (isPublicEndpoint(request)) {
            key = resolveClientIp(request);
            bucket = publicBuckets.get(key, k -> createPublicBucket());
        } else {
            key = resolveUserIdentity();
            if (key == null) {
                key = resolveClientIp(request);
            }
            bucket = authenticatedBuckets.get(key, k -> createAuthenticatedBucket());
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            long retryAfterSeconds = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill());
            if (retryAfterSeconds == 0) {
                retryAfterSeconds = 1;
            }
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.getWriter().write(
                    "{\"error\":\"Too many requests\",\"retryAfterSeconds\":" + retryAfterSeconds + "}"
            );
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return HttpMethod.POST.name().equals(method)
                && ("/user".equals(uri) || "/auth/login".equals(uri));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolveUserIdentity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }

    private Bucket createPublicBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(properties.getPublic().getRequestsPerMinute())
                        .refillGreedy(properties.getPublic().getRequestsPerMinute(), Duration.ofMinutes(1)))
                .build();
    }

    private Bucket createAuthenticatedBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(properties.getAuthenticated().getRequestsPerMinute())
                        .refillGreedy(properties.getAuthenticated().getRequestsPerMinute(), Duration.ofMinutes(1)))
                .build();
    }
}