package IxLambdaBackend.service;

import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.POST;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class MethodResolver {
    final Set<Method> getMethods;
    final Set<Method> postMethods;

    final Map<Template, Method> templateMethodMap = new HashMap<>();

    public void init() {
        for (final Method m: getMethods) {
            templateMethodMap.put(new Template(m.getAnnotation(GET.class).path()), m);
        }

        for (final Method m: postMethods) {
            templateMethodMap.put(new Template(m.getAnnotation(POST.class).path()), m);
        }
    }

    public Optional<MethodResponse> resolve(final String path) {
        for (final Map.Entry<Template, Method> entry: templateMethodMap.entrySet()) {
            Optional<Map<String, String>> pathAttributes = entry.getKey().parse(path);
            if (! pathAttributes.isPresent()) continue;

            return Optional.of(new MethodResponse(entry.getValue(), pathAttributes.get()));
        }

        return Optional.empty();
    }

    @AllArgsConstructor
    @Getter
    class MethodResponse {
        private final Method method;
        private final Map<String, String> pathParameters;
    }
}
