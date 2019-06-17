package IxLambdaBackend;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.SampleGetUserByIdActivity;
import IxLambdaBackend.exception.UnknownOperationException;
import IxLambdaBackend.request.Request;

import static IxLambdaBackend.TestData.API_getUserById;

public class ActivityFactory {
    public static Activity newInstance(final String api, final Request request) throws UnknownOperationException {
        switch (api) {
            case API_getUserById:
                return new SampleGetUserByIdActivity(request);
        }

        throw new UnknownOperationException("Unknown API: " + api);
    }
}
