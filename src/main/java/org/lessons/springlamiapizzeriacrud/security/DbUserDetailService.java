package org.lessons.springlamiapizzeriacrud.security;

import org.lessons.springlamiapizzeriacrud.model.User;
import org.lessons.springlamiapizzeriacrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DbUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findByEmail(username);
        if(result.isEmpty()) {
            throw new UsernameNotFoundException("User with email "+username+"not found.");
        }

        return new DbUserDetails(result.get());
    }
}
