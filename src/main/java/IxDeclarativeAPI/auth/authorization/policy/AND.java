package IxDeclarativeAPI.auth.authorization.policy;

import IxDeclarativeAPI.activity.Parameter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class AND implements Policy {
    final List<Policy> policies;

    @Override
    public boolean verify(@NonNull final Map<String, Parameter> attributeMap) {
        if (policies == null) return false;

        for (final Policy policy: this.policies) {
            if (! policy.verify(attributeMap)) return false;
        }

        return true;
    }
}
