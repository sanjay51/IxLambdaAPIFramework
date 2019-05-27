package IxDeclarativeAPI.auth.authorization;

import IxDeclarativeAPI.activity.Parameter;
import IxDeclarativeAPI.auth.AuthStrategy;
import IxDeclarativeAPI.auth.authorization.policy.Policy;
import IxDeclarativeAPI.exception.NotAuthorizedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class Authorization extends AuthStrategy {
    final List<Policy> policies;

    @Override
    public void execute(@NonNull final Map<String, Parameter> parameterMap) {
        // verify all policy with OR strategy

        for(final Policy policy: policies) {
            if (policy.verify(parameterMap)) {
                return; // Success; if any of the policy allows access
            }
        }

        throw new NotAuthorizedException("Authorization failed.");
    }
}
