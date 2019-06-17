package IxLambdaBackend;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.auth.AuthenticationContext;
import IxLambdaBackend.request.Params;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.util.TokenUtils;

import java.util.LinkedHashMap;

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
        final LinkedHashMap<String, String> queryString = new LinkedHashMap<>();

        queryString.put(PARAM_userId, TEST_USER_ID);
        queryString.put(PARAM_authToken, "test");

        params.setQuerystring(queryString);
        request.setQueryStringParameters(queryString);

        return request;
    }

    public static final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, sampleGetUserByIdRequest);
}
