package IxLambdaBackend.auth;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;
import IxLambdaBackend.util.TokenUtils;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Authentication extends AuthStrategy {
    final String userIdAttributeName;
    final String authTokenAttributeName;
    final AuthenticationContext context;

    @Override
    public void execute(final ParameterMap parameterMap) {
        TokenUtils.verifyToken(parameterMap.get(authTokenAttributeName).getStringValue(),
                parameterMap.get(userIdAttributeName).getStringValue(),
                this.context);
    }
}
