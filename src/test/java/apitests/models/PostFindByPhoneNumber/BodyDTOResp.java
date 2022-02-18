package apitests.models.PostFindByPhoneNumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BodyDTOResp {
    @JsonProperty("customerId")
    private String customerId;
}
