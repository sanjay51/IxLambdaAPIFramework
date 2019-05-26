package IxDeclarativeAPI.validator.param;

import IxDeclarativeAPI.activity.Parameter;
import com.amazonaws.util.StringUtils;

public class StringNotBlankValidator implements ParamValidator {

    @Override
    public ValidationResponse isValid(final Parameter param) {
        if (isBlank((String) param.getValue()))
            return new ValidationResponse(false, param.getName() + " cannot be blank.");

        return new ValidationResponse(true, null);
    }

    private boolean isBlank(final String input) {
        return StringUtils.isNullOrEmpty(StringUtils.trim(input));
    }
}