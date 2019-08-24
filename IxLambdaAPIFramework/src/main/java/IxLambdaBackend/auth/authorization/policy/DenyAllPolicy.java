package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class DenyAllPolicy implements Policy {

    @Override
    public boolean verify(final ParameterMap attributeMap) {
        return false; // deny all
    }
}
