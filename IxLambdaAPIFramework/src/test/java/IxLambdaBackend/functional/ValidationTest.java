package IxLambdaBackend.functional;

import IxLambdaBackend.ActivityFactory;
import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.exception.InvalidInputException;
import IxLambdaBackend.request.Request;
import org.junit.jupiter.api.Test;

import static IxLambdaBackend.TestData.*;
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
        request.getQueryStringParameters().put(PARAM_userId, null);
        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);

        assertThrows(InvalidInputException.class, () -> sampleGetUserByIdActivity.getResponse());
    }

    @Test
    public void assertExceptionForBlankInputValue() {
        final Request request = getSampleRequest();
        request.getQueryStringParameters().put(PARAM_userId, "");
        final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, request);

        assertThrows(InvalidInputException.class, () -> sampleGetUserByIdActivity.getResponse());
    }
}
