package IxDeclarativeAPI;

import IxDeclarativeAPI.activity.Activity;
import IxDeclarativeAPI.activity.Parameter;
import IxDeclarativeAPI.request.Params;
import IxDeclarativeAPI.request.Request;
import IxDeclarativeAPI.response.Response;
import IxDeclarativeAPI.validator.param.StringNotBlankValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public static final String API_getUserById = "GetUserById";
    public static final String PARAM_userId = "userId";
    public static final String PARAM_authId = "authId";
    public static final Request sampleGetUserByIdRequest = getSampleRequest();

    public static Request getSampleRequest() {
        Request request = new Request();

        final Params params = new Params();
        final Map<String, String> queryString = new HashMap<>();

        queryString.put(PARAM_userId, "userId");
        queryString.put(PARAM_authId, "authId");

        params.setQuerystring(queryString);
        request.setParams(params);

        return request;
    }

    public static final Activity sampleGetUserByIdActivity = ActivityFactory.newInstance(API_getUserById, sampleGetUserByIdRequest);


    static final class SampleGetUserByIdActivity extends Activity {
        public SampleGetUserByIdActivity(Request request) {
            super(request);
        }

        @Override
        protected Response enact() throws Exception {
            return null;
        }

        @Override
        protected List<Parameter> getParameters() {
            final Parameter<String> userId = new Parameter<>(PARAM_userId, Arrays.asList(new StringNotBlankValidator()));
            final Parameter<String> authId = new Parameter<>(PARAM_authId, Arrays.asList(new StringNotBlankValidator()));

            return Arrays.asList(userId, authId);
        }
    }
}
