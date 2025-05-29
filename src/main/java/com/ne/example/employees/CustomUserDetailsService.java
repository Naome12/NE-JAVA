package com.ne.example.employees;

import com.ne.example.employees.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository empRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user =  empRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));
        return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
