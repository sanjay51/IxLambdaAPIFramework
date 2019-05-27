package IxDeclarativeAPI.functional;

import IxDeclarativeAPI.ActivityFactory;
import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.exception.InvalidInputException;
import IxDeclarativeAPI.request.Request;
import org.junit.jupiter.api.Test;

import static IxDeclarativeAPI.TestData.API_getUserById;
import static IxDeclarativeAPI.TestData.PARAM_userId;
import static IxDeclarativeAPI.TestData.getSampleRequest;
import static IxDeclarativeAPI.TestData.sampleGetUserByIdActivity;
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
