package com.example.DealerFlow.Security.RateLimiting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private final PublicLimit publicLimit = new PublicLimit();
    private final AuthenticatedLimit authenticated = new AuthenticatedLimit();
    private final CacheConfig cache = new CacheConfig();

    public PublicLimit getPublic() {
        return publicLimit;
    }

    public AuthenticatedLimit getAuthenticated() {
        return authenticated;
    }

    public CacheConfig getCache() {
        return cache;
    }

    public static class PublicLimit {
        private int requestsPerMinute = 5;

        public int getRequestsPerMinute() {
            return requestsPerMinute;
        }

        public void setRequestsPerMinute(int requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
        }
    }

    public static class AuthenticatedLimit {
        private int requestsPerMinute = 60;

        public int getRequestsPerMinute() {
            return requestsPerMinute;
        }

        public void setRequestsPerMinute(int requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
        }
    }

    public static class CacheConfig {
        private int maxSize = 10000;
        private int expireMinutes = 10;

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getExpireMinutes() {
            return expireMinutes;
        }

        public void setExpireMinutes(int expireMinutes) {
            this.expireMinutes = expireMinutes;
        }
    }
}