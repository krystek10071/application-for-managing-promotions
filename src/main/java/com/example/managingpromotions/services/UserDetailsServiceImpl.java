package com.example.managingpromotions.services;

import com.example.managingpromotions.models.UserApp;
import com.example.managingpromotions.models.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserApp> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));

        return user.map(UserDetailsImpl::new).get();
    }
}
