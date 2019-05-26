package IxDeclarativeAPI;

import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.exception.UnknownOperationException;
import IxDeclarativeAPI.request.Request;

import static IxDeclarativeAPI.Data.API_getUserById;

public class ActivityFactory {
    public static Activity newInstance(final String api, final Request request) throws UnknownOperationException {
        switch (api) {
            case API_getUserById:
                return new Data.SampleGetUserByIdActivity(request);
        }

        throw new UnknownOperationException("Unknown API: " + api);
    }
}
