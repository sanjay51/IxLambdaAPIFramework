package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;

import java.util.Map;

public interface Policy {
    boolean verify(final ParameterMap parameterMap);
}
