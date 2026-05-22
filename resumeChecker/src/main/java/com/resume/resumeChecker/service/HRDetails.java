package com.resume.resumeChecker.service;

import com.resume.resumeChecker.model.HR;
import com.resume.resumeChecker.repository.HRRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HRDetails {

    private final HRRepository hrRepository;
    private final PasswordEncoder passwordEncoder;

    public HRDetails(HRRepository hrRepository, PasswordEncoder passwordEncoder){
        this.hrRepository = hrRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public HR createHR(HR hr) {
        hr.setPassword(passwordEncoder.encode(hr.getPassword()));
        hr.setActive(true);
        return hrRepository.save(hr);
    }

    public HR updateHR(Long id, HR updated) {
        HR existing = hrRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        if (updated.getName() != null)
            existing.setName(updated.getName());

        if (updated.getUsername() != null)
            existing.setUsername(updated.getUsername());

        return hrRepository.save(existing);
    }

    public void softDeleteHR(Long id) {
        HR hr = hrRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        hr.setActive(false);
        hrRepository.save(hr);
    }
}