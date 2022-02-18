package apitests.models.PostLogin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReq {
    private String login;
    private String password;
}
