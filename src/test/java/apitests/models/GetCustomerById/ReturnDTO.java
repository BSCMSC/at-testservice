package apitests.models.GetCustomerById;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ReturnDTO {
    private String customerId;
    private String name;
    private String status;
    private Long phone;
    private Map<String, String> additionalParameters;
    private String pd;
}
