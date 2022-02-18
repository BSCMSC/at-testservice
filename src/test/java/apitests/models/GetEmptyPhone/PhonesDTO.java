package apitests.models.GetEmptyPhone;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhonesDTO {
    private Long phone;
    private String locale;
}
