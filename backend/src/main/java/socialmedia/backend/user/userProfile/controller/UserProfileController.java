package socialmedia.backend.user.userProfile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.user.userProfile.dtos.UpdateProfileDTO;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
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
        Long myUserId = this.jwtTokenUtils.getUserIdFromRequest(request);

        UserProfileEntity myUser =  null;
        try {
            myUser = this.userProfileService.getProfileById(myUserId);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("something went wrong in our server. we will fix it. Please try again later...");
        }

        return ResponseEntity.ok().body(myUser);
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyProfile(@RequestBody UpdateProfileDTO newData, HttpServletRequest request) {
        Long myUserId = this.jwtTokenUtils.getUserIdFromRequest(request);

        UserProfileEntity myUser =  null;
        try {
            myUser = this.userProfileService.getProfileById(myUserId);
            myUser = this.userProfileService.modifyProfile(myUserId, newData);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("something went wrong in our server. we will fix it. Please try again later...");
        }

        return ResponseEntity.ok().body(myUser);
    }
}
