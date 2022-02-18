package apitests.models.GetCustomerById;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerByIdResp {
    @SerializedName("return")
    private ReturnDTO returnCustomerId;
}
