package kz.iitu.springlab.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    public DatabaseUserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getRoles().toArray(new String[0]))
                .disabled(!user.isEnabled())
                .build();
    }
}
