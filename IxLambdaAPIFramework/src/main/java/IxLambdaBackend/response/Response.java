package IxLambdaBackend.response;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    static final Gson gson = new Gson(); // TODO note: By default Gson would discard null values, change that.

    boolean isBase64Encoded;
    int statusCode;
    Object headers;
    Object multiValueHeaders;
    Object body;

    public Response(Object body) {
        this.body = gson.toJson(body);
        this.statusCode = 200;
        this.isBase64Encoded = false;
    }
}