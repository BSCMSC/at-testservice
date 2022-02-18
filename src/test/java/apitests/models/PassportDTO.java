package apitests.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassportDTO {
    private String passportNumber;
    private String passportSeries;
}
