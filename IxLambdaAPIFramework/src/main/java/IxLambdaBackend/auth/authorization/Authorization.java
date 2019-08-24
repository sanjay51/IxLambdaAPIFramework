package IxLambdaBackend.auth.authorization;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;
import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.auth.authorization.policy.Policy;
import IxLambdaBackend.exception.NotAuthorizedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Authorization extends AuthStrategy {
    final Policy[] policies;

    public Authorization(final Policy ... policies) {
        this.policies = policies;
    }

    @Override
    public void execute(@NonNull final ParameterMap parameterMap) {
        // verify all policy with OR strategy

        for(final Policy policy: policies) {
            if (policy.verify(parameterMap)) {
                return; // Success; if any of the policy allows access
            }
        }

        throw new NotAuthorizedException("Authorization failed.");
    }
}
