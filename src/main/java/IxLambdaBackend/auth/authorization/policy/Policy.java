package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;

import java.util.Map;

public interface Policy {
    boolean verify(final Map<String, Parameter> attributeMap);
}
