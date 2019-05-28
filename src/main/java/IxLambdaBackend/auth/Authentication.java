package IxLambdaBackend.auth;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.util.TokenUtils;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Authentication extends AuthStrategy {
    final String userIdAttributeName;
    final String authTokenAttributeName;
    final AuthenticationContext context;

    @Override
    public void execute(final Map<String, Parameter> parameterMap) {
        TokenUtils.verifyToken(parameterMap.get(authTokenAttributeName).getStringValue(),
                parameterMap.get(userIdAttributeName).getStringValue(),
                this.context);
    }
}