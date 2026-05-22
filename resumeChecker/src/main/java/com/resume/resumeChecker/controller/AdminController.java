package com.resume.resumeChecker.controller;

import com.resume.resumeChecker.dto.HRauthRequest;
import com.resume.resumeChecker.model.HR;

import com.resume.resumeChecker.service.HRDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hr")

public class AdminController {

    private final HRDetails hrDetails;
     private final AuthenticationManager authenticationManager;

    public AdminController(HRDetails hrDetails, AuthenticationManager authenticationManager) {
        this.hrDetails = hrDetails;
        this.authenticationManager = authenticationManager;
    }

    // Create a new HR user
    @PostMapping
    public ResponseEntity<HR> createHR(@RequestBody HR hr) {
        HR created = hrDetails.createHR(hr);
        return ResponseEntity.ok(created);
    }

    // Update an existing HR user
    @PutMapping("/{id}")
    public ResponseEntity<HR> updateHR(@PathVariable Long id, @RequestBody HR hr) {
        HR updated = hrDetails.updateHR(id, hr);
        return ResponseEntity.ok(updated);
    }

    // Soft delete an HR user (deactivate)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHR(@PathVariable Long id) {
        hrDetails.softDeleteHR(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody HRauthRequest request
    ) {

        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

        return ResponseEntity.ok("Login successful");
    }

}
