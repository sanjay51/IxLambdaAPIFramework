package IxDeclarativeAPI;

import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.activity.Parameter;
import IxDeclarativeAPI.auth.AuthStrategy;
import IxDeclarativeAPI.auth.AuthenticationContext;
import IxDeclarativeAPI.request.Params;
import IxDeclarativeAPI.request.Request;
import IxDeclarativeAPI.response.Response;
import IxDeclarativeAPI.util.TokenUtils;
import IxDeclarativeAPI.validator.param.StringNotBlankValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {
    public static final String API_getUserById = "GetUserById";
    public static final String PARAM_userId = "userId";
    public static final String PARAM_requesterId = "requesterId";
    public static final String PARAM_requesterAccessLevel = "requesterAccessLevel";
    public static final String PARAM_authToken = "authToken";
    public static final Request sampleGetUserByIdRequest = getSampleRequest();
    public static final AuthenticationContext authenticationContext =
            new AuthenticationContext("key", "issuer", "id");
    public static final String TEST_USER_ID = "sanjay51";
    public static final String TEST_AUTH_TOKEN = TokenUtils.generateToken(TEST_USER_ID, authenticationContext);

    public static Request getSampleRequest() {
        Request request = new Request();

        final Params params = new Params();
        final Map<String, String> queryString = new HashMap<>();

        queryString.put(PARAM_userId, TEST_USER_ID);
        queryString.put(PARAM_authToken, "test");

        params.setQuerystring(queryString);
        request.setParams(params);

        return request;
    }

    public static final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, sampleGetUserByIdRequest);


    static final class SampleGetUserByIdActivity extends Activity {
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
}
