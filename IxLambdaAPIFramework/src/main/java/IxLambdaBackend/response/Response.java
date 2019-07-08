package IxLambdaBackend.response;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Response {
    static final Gson gson = new Gson(); // TODO note: By default Gson would discard null values, change that.
    private Map<String, String> headers = new HashMap<String, String>() {{
        put("Access-Control-Allow-Origin", "*");
        put("Access-Control-Allow-Headers", "Content-Type");
    }};

    boolean isBase64Encoded;
    int statusCode;
    Object multiValueHeaders;
    Object body;

    public Response(Object body) {
        this.body = gson.toJson(body);
        this.statusCode = 200;
        this.isBase64Encoded = false;
    }
}