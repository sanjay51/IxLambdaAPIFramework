package IxLambdaBackend.auth;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;

import java.util.Map;

public abstract class AuthStrategy {
    abstract public void execute(ParameterMap parameterMap);
}
