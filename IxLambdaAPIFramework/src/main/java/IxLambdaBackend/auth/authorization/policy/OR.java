package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class OR implements Policy {
    final Policy[] policies;

    public static OR of(final Policy ... policies) {
        return new OR(policies);
    }

    @Override
    public boolean verify(@NonNull final Map<String, Parameter> attributeMap) {
        if (policies == null) return false;

        for (final Policy policy: this.policies) {
            if (policy.verify(attributeMap)) return true;
        }

        return false;
    }
}
