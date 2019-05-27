package IxLambdaBackend.functional;

import IxLambdaBackend.ActivityFactory;
import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.auth.Authentication;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.request.Request;
import org.junit.jupiter.api.Test;

import static IxLambdaBackend.TestData.API_getUserById;
import static IxLambdaBackend.TestData.PARAM_authToken;
import static IxLambdaBackend.TestData.PARAM_userId;
import static IxLambdaBackend.TestData.TEST_AUTH_TOKEN;
import static IxLambdaBackend.TestData.authenticationContext;
import static IxLambdaBackend.TestData.getSampleRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthenticationTest {

    @Test
    public void testActivityWithAuth() {
        final Authentication authenticationStrategy = new Authentication(PARAM_userId, PARAM_authToken, authenticationContext);

        final Request request = getSampleRequest();
        request.getParams().getQuerystring().put(PARAM_authToken, TEST_AUTH_TOKEN);

        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);
        sampleGetUserByIdActivity.getAuthStrategies().add(authenticationStrategy);

        assertDoesNotThrow(() -> sampleGetUserByIdActivity.getResponse());
    }

    @Test
    public void assertExceptionOnBadAuthToken() {
        final Authentication authenticationStrategy = new Authentication(PARAM_userId, PARAM_authToken, authenticationContext);

        final Request request = getSampleRequest();
        request.getParams().getQuerystring().put(PARAM_authToken, "blah blah");

        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);
        sampleGetUserByIdActivity.getAuthStrategies().add(authenticationStrategy);

        assertThrows(NotAuthorizedException.class, () -> sampleGetUserByIdActivity.getResponse());
    }
}
