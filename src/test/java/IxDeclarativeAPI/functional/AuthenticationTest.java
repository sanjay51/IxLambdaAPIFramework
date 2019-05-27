package IxDeclarativeAPI.functional;

import IxDeclarativeAPI.ActivityFactory;
import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.auth.Authentication;
import IxDeclarativeAPI.exception.NotAuthorizedException;
import IxDeclarativeAPI.request.Request;
import org.junit.jupiter.api.Test;

import static IxDeclarativeAPI.TestData.API_getUserById;
import static IxDeclarativeAPI.TestData.PARAM_authToken;
import static IxDeclarativeAPI.TestData.PARAM_userId;
import static IxDeclarativeAPI.TestData.TEST_AUTH_TOKEN;
import static IxDeclarativeAPI.TestData.authenticationContext;
import static IxDeclarativeAPI.TestData.getSampleRequest;
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
