package socialmedia.backend.user.userAuth.dtos;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String password;
}
