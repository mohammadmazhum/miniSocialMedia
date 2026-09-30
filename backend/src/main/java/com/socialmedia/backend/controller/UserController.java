package com.socialmedia.backend.controller;

import com.socialmedia.backend.model.ProfileResponse;
import com.socialmedia.backend.model.UpdateProfileRequest;
import com.socialmedia.backend.model.User;
import com.socialmedia.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already exists");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(
                createSafeUserResponse(savedUser)
        );
    }

    // Current user
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(user ->
                        ResponseEntity.ok(
                                createSafeUserResponse(user)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

    // Get another user's profile
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable String email) {

        return userRepository.findByEmail(email)
                .map(user ->
                        ResponseEntity.ok(
                                createSafeUserResponse(user)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/{username}/followers")
//    public ResponseEntity<?> getFollowers(
//            @PathVariable String username) {
//
//        return userRepository.findByUsername(username)
//                .map(user ->
//                        ResponseEntity.ok(user.getFollowers())
//                )
//                .orElse(ResponseEntity.notFound().build());
//    }

    // Following
//    @GetMapping("/{username}/following")
//    public ResponseEntity<?> getFollowing(
//            @PathVariable String username) {
//
//        return userRepository.findByUsername(username)
//                .map(user ->
//                        ResponseEntity.ok(user.getFollowing())
//                )
//                .orElse(ResponseEntity.notFound().build());
//    }

    private Map<String, Object> createSafeUserResponse(User user) {

        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "bio", user.getBio() == null ? "" : user.getBio(),
                "profileImage",
                user.getProfileImage() == null
                        ? ""
                        : user.getProfileImage(),
                "followers", user.getFollowers(),
                "following", user.getFollowing()
        );
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(
            @PathVariable String username) {

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        ProfileResponse profile = new ProfileResponse(
                user.getUsername(),
                user.getBio(),
                user.getFollowers() == null
                        ? 0
                        : user.getFollowers().size(),
                user.getFollowing() == null
                        ? 0
                        : user.getFollowing().size()
        );

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<?> getMyProfile(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        ProfileResponse profile = new ProfileResponse(
                user.getUsername(),
                user.getBio(),
                user.getFollowers() == null
                        ? 0
                        : user.getFollowers().size(),
                user.getFollowing() == null
                        ? 0
                        : user.getFollowing().size()
        );

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setBio(request.getBio());

        userRepository.save(user);

        return ResponseEntity.ok(
                "Profile updated successfully"
        );
    }

//    @PostMapping("/{username}/follow")
//    public ResponseEntity<?> followUser(
//            @PathVariable String username,
//            Authentication authentication) {
//
//        String currentEmail = authentication.getName();
//
//        User currentUser = userRepository
//                .findByEmail(currentEmail)
//                .orElse(null);
//
//        User targetUser = userRepository
//                .findByUsername(username)
//                .orElse(null);
//
//        if (currentUser == null || targetUser == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (currentUser.getUsername()
//                .equals(targetUser.getUsername())) {
//
//            return ResponseEntity
//                    .badRequest()
//                    .body("You cannot follow yourself");
//        }
//
//        if (currentUser.getFollowing() == null) {
//            currentUser.setFollowing(new ArrayList<>());
//        }
//
//        if (targetUser.getFollowers() == null) {
//            targetUser.setFollowers(new ArrayList<>());
//        }
//
//        String targetUsername = targetUser.getUsername();
//        String currentUsername = currentUser.getUsername();
//
//        // Already following → unfollow
//        if (currentUser.getFollowing().contains(targetUsername)) {
//
//            currentUser.getFollowing().remove(targetUsername);
//            targetUser.getFollowers().remove(currentUsername);
//
//            userRepository.save(currentUser);
//            userRepository.save(targetUser);
//
//            return ResponseEntity.ok("Unfollowed successfully");
//        }
//
//        // Not following → follow
//        currentUser.getFollowing().add(targetUsername);
//        targetUser.getFollowers().add(currentUsername);
//
//        userRepository.save(currentUser);
//        userRepository.save(targetUser);
//
//        return ResponseEntity.ok("Followed successfully");
//    }
}