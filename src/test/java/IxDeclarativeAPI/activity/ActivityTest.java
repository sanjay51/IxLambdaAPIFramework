package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.ActivityFactory;
import IxDeclarativeAPI.auth.Authentication;
import IxDeclarativeAPI.exception.InvalidInputException;
import IxDeclarativeAPI.exception.NotAuthorizedException;
import IxDeclarativeAPI.request.Request;
import org.junit.jupiter.api.Test;

import static IxDeclarativeAPI.TestData.API_getUserById;
import static IxDeclarativeAPI.TestData.PARAM_authToken;
import static IxDeclarativeAPI.TestData.PARAM_userId;
import static IxDeclarativeAPI.TestData.TEST_AUTH_TOKEN;
import static IxDeclarativeAPI.TestData.authenticationContext;
import static IxDeclarativeAPI.TestData.getSampleRequest;
import static IxDeclarativeAPI.TestData.sampleGetUserByIdActivity;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActivityTest {
    @Test
    public void testBaseCase() throws Exception {
        sampleGetUserByIdActivity.getResponse();
    }

    @Test
    public void assertExceptionForNullInputValue() {
        final Request request = getSampleRequest();
        request.getParams().getQuerystring().put(PARAM_userId, null);
        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);

        assertThrows(InvalidInputException.class, () -> sampleGetUserByIdActivity.getResponse());
    }

    @Test
    public void assertExceptionForBlankInputValue() {
        final Request request = getSampleRequest();
        request.getParams().getQuerystring().put(PARAM_userId, "");
        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);

        assertThrows(InvalidInputException.class, () -> sampleGetUserByIdActivity.getResponse());
    }

    @Test
    public void testActivityWithAuth() throws Exception {
        final Authentication authenticationStrategy = new Authentication(PARAM_userId, PARAM_authToken, authenticationContext);

        final Request request = getSampleRequest();
        request.getParams().getQuerystring().put(PARAM_authToken, TEST_AUTH_TOKEN);

        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);
        sampleGetUserByIdActivity.getAuthStrategies().add(authenticationStrategy);

        sampleGetUserByIdActivity.getResponse();
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
