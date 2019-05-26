package IxDeclarativeAPI.util;

import IxDeclarativeAPI.auth.AuthenticationContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.NonNull;

public class TokenUtils {
    public static String generateToken(@NonNull final String claimId,
                                       @NonNull final AuthenticationContext context) {
        String token = JWT.create()
                .withIssuer(context.getIssuer())
                .withClaim(context.getClaimId(), claimId)
                .sign(Algorithm.HMAC256(context.getKey().getBytes()));

        return token;

    }

    public static void verifyToken(@NonNull final String token,
                                   @NonNull final String claimId,
                                   @NonNull final AuthenticationContext context) {
        final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(context.getKey().getBytes()))
                .withIssuer(context.getIssuer())
                .withClaim(context.getClaimId(), claimId)
                .build();
        verifier.verify(token);
    }
}
