package com.example.DealerFlow.Security;

import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                resolveAuthorities(user)
        );
    }

    /**
     * Builds the GrantedAuthority set from the user's role and the permissions
     * attached to it.
     *
     * The role is exposed with the conventional {@code ROLE_} prefix so that
     * {@code hasRole('ADMIN')} keeps working, while permissions are exposed
     * as plain authorities so that {@code hasAuthority('CanAccessCarModelData')}
     * can be used on endpoints.
     */
    private Set<GrantedAuthority> resolveAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        Role role = user.getRole();
        if (role == null) {
            return authorities;
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        if (role.getPermissions() != null) {
            role.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getName()))
            );
        }

        return authorities;
    }
}
