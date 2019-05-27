package IxLambdaBackend.functional;

import IxLambdaBackend.ActivityFactory;
import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.exception.InvalidInputException;
import IxLambdaBackend.request.Request;
import org.junit.jupiter.api.Test;

import static IxLambdaBackend.TestData.API_getUserById;
import static IxLambdaBackend.TestData.PARAM_userId;
import static IxLambdaBackend.TestData.getSampleRequest;
import static IxLambdaBackend.TestData.sampleGetUserByIdActivity;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTest {
    @Test
    public void testBaseCase() {
        assertDoesNotThrow(() -> sampleGetUserByIdActivity.getResponse());
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
}
