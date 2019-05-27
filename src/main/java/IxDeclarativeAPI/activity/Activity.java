package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.auth.AuthStrategy;
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
    private Map<String, Parameter> parameterMap = new HashMap<>();

    protected abstract Response enact() throws Exception;
    protected abstract List<Parameter> getParameters();
    public abstract List<AuthStrategy> getAuthStrategies();

    public Activity(final Request request) {
        this.request = request;
    }

    private void initialize() {
        final Map<String, String> queryParams = this.request.getParams().getQuerystring();

        this.getParameters().stream().forEach(parameter -> {
            parameter.withValue(queryParams.get(parameter.getName()));
            this.parameterMap.put(parameter.getName(), parameter);
        });
    }

    protected void validateRequest() throws InvalidInputException {
        // Validate parameters one by one
        for (final String paramName: this.parameterMap.keySet()) {
            final Parameter parameter = this.parameterMap.get(paramName);

            final ValidationResponse validationResponse = parameter.validate();
            if (! validationResponse.isValid()) {
                throw new InvalidInputException("Authentication/authorization failed.");
            }
        }
    }

    protected void checkAuth() throws NotAuthorizedException {
        if (this.getAuthStrategies() == null) return;

        try {
            this.getAuthStrategies().stream().forEach(authStrategy -> {
                authStrategy.execute(this.parameterMap);
            });
        } catch (final Exception e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    public Response getResponse() throws Exception {
        this.initialize();
        this.validateRequest();
        this.checkAuth();
        return this.enact();
    }

    public Parameter getParameterByName(@NonNull final String name) {
        return this.parameterMap.get(name);
    }

    public void addParameter(final Parameter parameter) {
        this.parameterMap.put(parameter.getName(), parameter);
    }
}
