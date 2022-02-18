package apitests.models.PostFindByPhoneNumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HeaderDTO {
    @JsonProperty("authToken")
    private String authToken;
}
