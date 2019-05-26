package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.validator.param.ParamValidator;
import IxDeclarativeAPI.validator.param.ValidationResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class Parameter<T> {
    private String name;
    private T value;
    private List<ParamValidator> validators;

    public Parameter(final String name, final List<ParamValidator> validators) {
        this.name = name;
        this.validators = validators;
    }

    public ValidationResponse validate() {
        for (final ParamValidator validator: this.validators) {
            final ValidationResponse response = validator.isValid(this);

            if (!response.isValid()) return response;
        }

        return new ValidationResponse(true, "");
    }

    public void setValue(Object value) {
        this.value = (T) value;
    }

    public String getStringValue() {
        return (String) value;
    }
}
