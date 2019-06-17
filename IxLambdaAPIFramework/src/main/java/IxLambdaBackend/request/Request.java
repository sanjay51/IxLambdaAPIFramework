package IxLambdaBackend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@NoArgsConstructor
@Getter
@Setter
public class Request {
    private String resource;
    private String path;
    private String httpMethod;
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, Object> multiValueHeaders;
    private LinkedHashMap<String, String> queryStringParameters;
    private LinkedHashMap<String, Object> multiValueQueryStringParameters;
    private String body;

    @Override
    public String toString() {
        return " --- path --- \n" +
                this.path +
                "\n --- http method --- \n" +
                this.httpMethod +
                "\n --- body --- \n" +
                this.body;
    }
}
