package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class DenyAllPolicy implements Policy {

    @Override
    public boolean verify(final Map<String, Parameter> attributeMap) {
        return false; // deny all
    }
}
