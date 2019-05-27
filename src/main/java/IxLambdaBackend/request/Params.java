package IxLambdaBackend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class Params {
    private Path path;
    private Map<String, String> querystring;
    private Header header;

    @Override
    public String toString() {
        return "Params: " + Arrays.toString(this.querystring.entrySet().toArray());
    }
}
