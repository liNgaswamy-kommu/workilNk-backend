package com.workilnk.user;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.workilnk.user.dto.UpdateProfileRequest;
import com.workilnk.user.dto.UserBasicResponse;
import com.workilnk.user.dto.UserProfileResponse;
import com.workilnk.util.FileStorageService;
import com.workilnk.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    // SIGNUP
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // OTHER USER BASIC PROFILE
    @GetMapping("/{userId}")
    public ResponseEntity<UserBasicResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserBasic(userId));
    }

    // 🔥 LOGGED-IN USER PROFILE (USED BY FRONTEND)
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        Long userId = SecurityUtil.getLoggedInUserId();
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    // 🔥 UPDATE PROFILE (IMPORTANT)
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @RequestBody UpdateProfileRequest request) {

        Long userId = SecurityUtil.getLoggedInUserId();
        return ResponseEntity.ok(userService.updateMyProfile(userId, request));
    }

    // UPLOAD PROFILE PHOTO
    @PostMapping("/me/profile-pic")
    public ResponseEntity<String> uploadProfilePhoto(
            @RequestParam("file") MultipartFile file) {

        Long userId = SecurityUtil.getLoggedInUserId();

        String photoUrl = fileStorageService.storeFile(file);
        userService.updateProfilePhoto(userId, photoUrl);

        return ResponseEntity.ok(photoUrl);
    }

    // REMOVE PROFILE PHOTO
    @DeleteMapping("/me/profile-pic")
    public ResponseEntity<Void> removeProfilePic() throws IOException {

        Long userId = SecurityUtil.getLoggedInUserId();
        userService.removeProfilePic(userId);

        return ResponseEntity.ok().build();
    }
}
