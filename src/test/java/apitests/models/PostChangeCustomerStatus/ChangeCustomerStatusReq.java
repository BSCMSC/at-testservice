package apitests.models.PostChangeCustomerStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeCustomerStatusReq {
    private String status;
}
