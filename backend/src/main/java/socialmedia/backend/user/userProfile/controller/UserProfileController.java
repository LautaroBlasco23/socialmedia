package socialmedia.backend.user.userProfile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.user.userProfile.dtos.UpdateProfileDTO;
import socialmedia.backend.user.userProfile.dtos.UserReturnDTO;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.service.UserProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserProfileController {
    
    private final UserProfileService userProfileService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/")
    public ResponseEntity<?> getAllProfiles() {
        return ResponseEntity.ok(this.userProfileService.getAllProfiles());
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        try {
            Long myUserId = jwtTokenUtils.getUserIdFromRequest(request);
            UserReturnDTO user = userProfileService.getProfileById(myUserId);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("server error");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyProfile(@PathVariable Long userId) {
        try {
            UserReturnDTO user = userProfileService.getProfileById(userId);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("user not found");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("server error");
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyProfile(@RequestBody UpdateProfileDTO newData, HttpServletRequest request) {
        try {
            Long myUserId = this.jwtTokenUtils.getUserIdFromRequest(request);
            UserReturnDTO user = userProfileService.modifyProfile(myUserId, newData);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("server error");
        }
    }
}
