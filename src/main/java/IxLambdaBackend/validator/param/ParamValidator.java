package IxLambdaBackend.validator.param;

import IxLambdaBackend.activity.Parameter;

public interface ParamValidator {
    ValidationResponse isValid(Parameter param);
}
