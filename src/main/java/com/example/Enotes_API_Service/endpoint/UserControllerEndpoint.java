package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.PasswordChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User", description = "APIs for managing user")
@RequestMapping("/api/v1/user")
public interface UserControllerEndpoint {

    @Operation(summary = "Get profile", tags = {"User"}, description = "Retrieve user profile details")
    @GetMapping("/profile")
    ResponseEntity<?> getProfile();

    @Operation(summary = "Change password", tags = {"User"}, description = "Change user old password to new")
    @PostMapping("/change/password")
    ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request);
}
