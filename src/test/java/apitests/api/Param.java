package apitests.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Getter
@Accessors(chain = true, fluent = true)
public class Param {
    private String key;
    private Object value;
}
