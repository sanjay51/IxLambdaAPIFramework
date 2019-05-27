package IxLambdaBackend.auth;

import IxLambdaBackend.activity.Parameter;

import java.util.Map;

public abstract class AuthStrategy {
    abstract public void execute(Map<String, Parameter> parameterMap);
}
