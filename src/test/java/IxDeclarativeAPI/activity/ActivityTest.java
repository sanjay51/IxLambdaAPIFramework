package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.ActivityFactory;
import IxDeclarativeAPI.exception.InvalidInputException;
import IxDeclarativeAPI.request.Request;
import org.junit.jupiter.api.Test;

import static IxDeclarativeAPI.Data.API_getUserById;
import static IxDeclarativeAPI.Data.PARAM_userId;
import static IxDeclarativeAPI.Data.getSampleRequest;
import static IxDeclarativeAPI.Data.sampleGetUserByIdActivity;
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
}
