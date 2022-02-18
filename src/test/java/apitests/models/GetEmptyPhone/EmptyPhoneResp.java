package apitests.models.GetEmptyPhone;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmptyPhoneResp {
    private List<PhonesDTO> phones;
}
