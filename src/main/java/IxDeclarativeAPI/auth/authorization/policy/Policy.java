package IxDeclarativeAPI.auth.authorization.policy;

import IxDeclarativeAPI.activity.Parameter;

import java.util.Map;

public interface Policy {
    boolean verify(final Map<String, Parameter> attributeMap);
}
