package IxDeclarativeAPI;

import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.exception.UnknownOperationException;
import IxDeclarativeAPI.request.Request;

import static IxDeclarativeAPI.TestData.API_getUserById;

public class ActivityFactory {
    public static Activity newInstance(final String api, final Request request) throws UnknownOperationException {
        switch (api) {
            case API_getUserById:
                return new TestData.SampleGetUserByIdActivity(request);
        }

        throw new UnknownOperationException("Unknown API: " + api);
    }
}
