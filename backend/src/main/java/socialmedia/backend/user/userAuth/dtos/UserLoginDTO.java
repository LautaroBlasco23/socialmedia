package socialmedia.backend.user.userAuth.dtos;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
