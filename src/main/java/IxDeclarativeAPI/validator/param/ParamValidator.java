package IxDeclarativeAPI.validator.param;

import IxDeclarativeAPI.activity.Parameter;

public interface ParamValidator {
    ValidationResponse isValid(Parameter param);
}
