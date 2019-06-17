package IxLambdaBackend.activity;

import IxLambdaBackend.auth.AuthStrategy;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.response.Response;
import IxLambdaBackend.validator.param.StringNotBlankValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleGetUserByIdActivity extends Activity {
    public static final String PARAM_userId = "userId";
    public static final String PARAM_authToken = "authToken";

    public List<AuthStrategy> authStrategies = new ArrayList<>();

    public SampleGetUserByIdActivity(final Request request) {
        super(request);
    }

    @Override
    protected Response enact() throws Exception {
        return null;
    }

    @Override
    protected List<Parameter> getParameters() {
        final Parameter<String> userId = new Parameter<>(PARAM_userId, Arrays.asList(new StringNotBlankValidator()));
        final Parameter<String> authToken = new Parameter<>(PARAM_authToken, Arrays.asList(new StringNotBlankValidator()));

        return Arrays.asList(userId, authToken);
    }

    @Override
    public List<AuthStrategy> getAuthStrategies() {
        return authStrategies;
    }
}
