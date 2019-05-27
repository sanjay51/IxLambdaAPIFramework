package IxLambdaBackend.request;

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
}
