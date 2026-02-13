package com.workilnk.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.user.dto.UpdateProfileRequest;
import com.workilnk.user.dto.UserBasicResponse;
import com.workilnk.user.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // CREATE USER
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    // INTERNAL GET USER
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // BASIC PROFILE
    public UserBasicResponse getUserBasic(Long userId) {

        User user = getUser(userId);

        UserBasicResponse dto = new UserBasicResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        return dto;
    }

    // 🔥 FULL PROFILE (FRONTEND USE)
    public UserProfileResponse getMyProfile(Long userId) {

        User user = getUser(userId);
        return mapToProfileDto(user);
    }

    // 🔥 UPDATE PROFILE (THIS WAS MISSING EARLIER)
    public UserProfileResponse updateMyProfile(
            Long userId, UpdateProfileRequest request) {

        User user = getUser(userId);

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setBio(request.getBio());
        user.setAddress(request.getAddress());

        userRepository.save(user);
        return mapToProfileDto(user);
    }

    // UPDATE PROFILE PHOTO
    public void updateProfilePhoto(Long userId, String photoUrl) {

        User user = getUser(userId);
        user.setProfilePic(photoUrl);
        userRepository.save(user);
    }

    // REMOVE PROFILE PHOTO
    public void removeProfilePic(Long userId) {

        User user = getUser(userId);

        try {
            if (user.getProfilePic() != null) {

                Path path = Paths.get(uploadDir + user.getProfilePic());
                Files.deleteIfExists(path);

                user.setProfilePic(null);
                userRepository.save(user);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to remove profile photo", e);
        }
    }

    // 🔁 COMMON MAPPER
    private UserProfileResponse mapToProfileDto(User user) {

        UserProfileResponse dto = new UserProfileResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setBio(user.getBio());
        dto.setAddress(user.getAddress());
        dto.setProfilePic(user.getProfilePic());
        dto.setRole(user.getRole().name());

        return dto;
    }
}
