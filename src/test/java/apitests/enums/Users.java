package apitests.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Users {
    ADMIN("admin", "password"),
    USER("user", "password");
    private String login;
    private String password;
}
