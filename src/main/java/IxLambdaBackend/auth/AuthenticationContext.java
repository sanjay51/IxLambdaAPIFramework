package IxLambdaBackend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationContext {
    private String key; // Secret key
    private String issuer; // Token issuer
    private String claimId; // Identifier of the claimant
}
