package IxDeclarativeAPI.validator.param;

import IxDeclarativeAPI.activity.Parameter;

public class NotNullValidator implements ParamValidator {

    @Override
    public ValidationResponse isValid(final Parameter param) {
        if (param.getValue() == null)
            return new ValidationResponse(false, param.getName() + " cannot be null or empty.");

        return new ValidationResponse(true, null);
    }
}
