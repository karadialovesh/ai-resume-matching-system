package com.resume.resumeChecker.service;

import com.resume.resumeChecker.model.HR;
import com.resume.resumeChecker.repository.HRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HRDetailsService implements UserDetailsService {

    private final HRRepository hrRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HR hr = hrRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("HR not found"));
        return new User(
                hr.getUsername(),
                hr.getPassword(),
                List.of(new SimpleGrantedAuthority(hr.getRole()))
        );
    }


}
