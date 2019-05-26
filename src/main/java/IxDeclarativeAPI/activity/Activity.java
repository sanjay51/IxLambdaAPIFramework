package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.exception.InvalidInputException;
import IxDeclarativeAPI.exception.NotAuthorizedException;
import IxDeclarativeAPI.request.Request;
import IxDeclarativeAPI.response.Response;
import IxDeclarativeAPI.validator.param.ValidationResponse;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Activity {
    final Request request;
    private Map<String, Parameter> parameterMap = null;

    protected abstract Response enact() throws Exception;
    protected abstract List<Parameter> getParameters();

    public Activity(Request request) {
        this.request = request;
    }

    private void initialize() {
        this.parameterMap = new HashMap<>();
        final Map<String, String> queryParams = this.request.getParams().getQuerystring();

        this.getParameters().stream().forEach(parameter -> {
            parameter.setValue(queryParams.get(parameter.getName()));
            this.parameterMap.put(parameter.getName(), parameter);
        });
    }

    public Response getResponse() throws Exception {
        this.initialize();
        this.validateRequest();
        this.checkAuth();
        return this.enact();
    }

    protected void validateRequest() throws InvalidInputException {
        // Validate parameters one by one
        for (final String paramName: this.parameterMap.keySet()) {
            final Parameter parameter = this.parameterMap.get(paramName);

            final ValidationResponse validationResponse = parameter.validate();
            if (! validationResponse.isValid()) {
                throw new InvalidInputException(validationResponse.getMessage());
            }
        }
    }

    protected void checkAuth() throws NotAuthorizedException {

    }


    public Parameter getParameterByName(@NonNull final String name) {
        return this.parameterMap.get(name);
    }
}
