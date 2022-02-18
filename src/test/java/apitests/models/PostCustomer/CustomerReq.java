package apitests.models.PostCustomer;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CustomerReq {
    private String name;
    private Long phone;
    private Map<String, String> additionalParameters;
}
