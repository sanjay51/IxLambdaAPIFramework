package IxDeclarativeAPI.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@NoArgsConstructor
@Getter
@Setter
public class Request {
    private LinkedHashMap<String, String> body;
    private Params params;
    private Context context;

    @Override
    public String toString() {
        return this.context.toString() +
                "\n --- \n" +
                this.params.toString() +
                "\n --- \n" +
                this.body.toString();
    }
}
