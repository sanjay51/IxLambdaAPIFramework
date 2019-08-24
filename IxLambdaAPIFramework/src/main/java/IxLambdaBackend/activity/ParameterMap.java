package IxLambdaBackend.activity;

import IxLambdaBackend.exception.InvalidInputException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParameterMap {
    private Map<String, Parameter> parameterMap = new HashMap<>();

    public void put(final String name, final Parameter value) {
        this.parameterMap.put(name, value);
    }

    public Parameter get(final String name) {
        if (! this.parameterMap.containsKey(name)) throw new InvalidInputException("Parameter `" + name + "`is undefined");

        return this.parameterMap.get(name);
    }

    public Set<String> keySet() {
        return this.parameterMap.keySet();
    }
}
