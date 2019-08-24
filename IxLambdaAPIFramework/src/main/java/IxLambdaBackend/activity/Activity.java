package IxLambdaBackend.activity;

import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.exception.InvalidInputException;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.ValidationResponse;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Activity {
    @Setter private Request request;
    @Setter Map<String, String> pathParameters = ImmutableMap.of();

    private Map<String, Parameter> parameterMap = new HashMap<>();

    protected abstract Response enact() throws Exception;
    protected abstract List<Parameter> getParameters();
    public abstract List<AuthStrategy> getAuthStrategies();

    public Activity(final Request request) {
        this.request = request;
    }

    public Activity() {
    }

    private void initialize() {
        final Map<String, String> queryAndBodyParams = this.request.getQueryOrBodyParameters();

        this.getParameters().stream().forEach(parameter -> {
            final String name = parameter.getName();

            if (pathParameters.containsKey(name)) {
                parameter.withValue(pathParameters.get(name));
            }

            if (queryAndBodyParams != null && queryAndBodyParams.containsKey(name)) {
                parameter.withValue(queryAndBodyParams.get(name));
            }

            this.parameterMap.put(parameter.getName(), parameter);
        });
    }

    protected void validateRequest() throws InvalidInputException {
        // Validate parameters one by one
        for (final String paramName: this.parameterMap.keySet()) {
            final Parameter parameter = this.parameterMap.get(paramName);

            final ValidationResponse validationResponse = parameter.validate();
            if (! validationResponse.isValid()) {
                throw new InvalidInputException("Input validation failed for - " + paramName + ": " + validationResponse);
            }
        }
    }

    protected void preprocess() throws Exception {

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
        this.preprocess();
        this.checkAuth();
        return this.enact();
    }

    public Parameter getParameterByName(@NonNull final String name) {
        return this.parameterMap.get(name);
    }

    public String getStringParameterByName(@NonNull final String name) {
        if (this.parameterMap.containsKey(name)) return this.parameterMap.get(name).getStringValue();
        return null;
    }

    public void addParameter(final Parameter parameter) {
        this.parameterMap.put(parameter.getName(), parameter);
    }
}
