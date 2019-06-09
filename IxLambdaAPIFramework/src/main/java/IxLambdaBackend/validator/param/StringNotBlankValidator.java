package IxLambdaBackend.validator.param;

import IxLambdaBackend.activity.Parameter;
import org.apache.commons.lang3.StringUtils;

public class StringNotBlankValidator implements ParamValidator {

    @Override
    public ValidationResponse isValid(final Parameter param) {
        if (isBlank((String) param.getValue()))
            return new ValidationResponse(false, param.getName() + " cannot be blank.");

        return new ValidationResponse(true, null);
    }

    private boolean isBlank(final String input) {
        return StringUtils.isBlank(input);
    }
}