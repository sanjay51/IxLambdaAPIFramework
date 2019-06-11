package IxLambdaBackend.request;

import com.amazonaws.HttpMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Context {
    private String httpMethod;

    @Override
    public String toString() {
        return this.httpMethod;
    }

    public HttpMethod getHttpMethod() {
        return HttpMethod.valueOf(this.httpMethod);
    }
}
