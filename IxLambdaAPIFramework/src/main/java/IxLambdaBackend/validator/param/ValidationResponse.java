package IxLambdaBackend.validator.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponse {
    final boolean isValid;
    final String message;

    public ValidationResponse(final boolean isValid) {
        this.isValid = isValid;
        this.message = "";
    }
}
