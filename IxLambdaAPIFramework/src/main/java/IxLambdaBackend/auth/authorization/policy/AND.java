package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AND implements Policy {
    final List<Policy> policies;

    @Override
    public boolean verify(@NonNull final ParameterMap attributeMap) {
        if (policies == null) return false;

        for (final Policy policy: this.policies) {
            if (! policy.verify(attributeMap)) return false;
        }

        return true;
    }
}
