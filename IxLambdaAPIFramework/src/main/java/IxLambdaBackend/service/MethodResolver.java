package IxLambdaBackend.service;

import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.PATCH;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.request.HttpMethod;
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
    final Set<Method> patchMethods;

    final Map<Template, Method> templateMethodMap = new HashMap<>();

    public void init() {
        for (final Method m: getMethods) {
            templateMethodMap.put(new Template(HttpMethod.GET, m.getAnnotation(GET.class).path()), m);
        }

        for (final Method m: postMethods) {
            templateMethodMap.put(new Template(HttpMethod.POST, m.getAnnotation(POST.class).path()), m);
        }

        for (final Method m: patchMethods) {
            templateMethodMap.put(new Template(HttpMethod.PATCH, m.getAnnotation(PATCH.class).path()), m);
        }
    }

    public Optional<MethodResponse> resolve(final HttpMethod httpMethod, final String path) {
        for (final Map.Entry<Template, Method> entry: templateMethodMap.entrySet()) {
            final Template template = entry.getKey();

            if (template.httpMethod != httpMethod) continue;

            Optional<Map<String, String>> pathAttributes = template.parse(path);
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
