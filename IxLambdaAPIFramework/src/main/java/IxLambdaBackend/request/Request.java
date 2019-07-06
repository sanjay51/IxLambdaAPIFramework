package IxLambdaBackend.request;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class Request {
    static final Gson gson = new Gson();

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

    public Map<String, String> getParameters() {
        if (StringUtils.isBlank(httpMethod)) return queryStringParameters;
        if (HttpMethod.GET.name().equals(httpMethod)) return queryStringParameters;

        return gson.fromJson(this.body, Map.class);
    }
}
