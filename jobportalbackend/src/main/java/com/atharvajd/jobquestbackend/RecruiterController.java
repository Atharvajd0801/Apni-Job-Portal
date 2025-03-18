package com.atharvajd.jobquestbackend;

import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recruiters")
@CrossOrigin(origins = "http://localhost:8080")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.allRecruiters());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Optional<Recruiter>> getSingleRecruiter(@PathVariable String email) {
        return ResponseEntity.ok(recruiterService.singleRecruiter(email));
    }

    @PostMapping("/{email}/appendjob")
    public ResponseEntity<?> appendJob(@PathVariable String email, @RequestBody String jobId) {
        try {
            return ResponseEntity.ok(recruiterService.addJobToRecruiter(email, jobId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PostMapping("/{email}/removejob")
    public ResponseEntity<Recruiter> removeJob(@PathVariable String email, @RequestBody String jobId) {
        return ResponseEntity.ok(recruiterService.removeJobFromRecruiter(email, jobId));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Recruiter recruiter) {
        Optional<Recruiter> existingRecruiter = recruiterService.singleRecruiter(recruiter.getEmail());
        if (existingRecruiter.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
        }
        recruiter.setPassword(passwordEncoder.encode(recruiter.getPassword())); // Ensure password is hashed
        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterService.createRecruiter(recruiter));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        return authenticateUser(payload.get("email"), payload.get("password"), request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    private ResponseEntity<Map<String, Object>> authenticateUser(String email, String password, HttpServletRequest request) {
        try {
            Optional<Recruiter> recruiter = recruiterService.singleRecruiter(email);
            if (recruiter.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not found"));
            }
            if (!passwordEncoder.matches(password, recruiter.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong password"));
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", session.getId());
            responseBody.put("recruiter", recruiter);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Authentication error"));
        }
    }
}
