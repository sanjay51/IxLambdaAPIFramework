package IxLambdaBackend.validator.param;

import lombok.Data;

@Data
public class ValidationResponse {
    final boolean isValid;
    final String message;
}
