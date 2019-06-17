package IxLambdaBackend.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.*;

public class Template {
    final List<TemplateElement> templateElements = new ArrayList<>();

    public Template(@NonNull final String template) {
        validate(template);

        final String[] pathElements = template.split("/");

        for (final String element : pathElements) {
            if (isAttribute(element)) {
                final String elementName = element.substring(1, element.length() - 1); // exclude { and }
                templateElements.add(new TemplateElement(ElementType.ATTRIBUTE, elementName));
            } else {
                templateElements.add(new TemplateElement(ElementType.FIXED, element));
            }
        }
    }

    public Optional<Map<String, String>> parse(@NonNull final String path) {
        validate(path);
        final String[] pathElements = path.split("/");
        if (pathElements.length != templateElements.size()) return Optional.empty();

        final Map<String, String> attributes = new HashMap<>();

        // ignore the 0th element, it'll always be empty
        for (int i = 1; i < templateElements.size(); i++) {
            final String element = pathElements[i];
            final TemplateElement templateElement = templateElements.get(i);

            switch (templateElement.type) {
                case FIXED:
                    if (!element.equals(templateElement.name)) return Optional.empty();
                    break;

                case ATTRIBUTE:
                    attributes.put(templateElement.name, element);
                    break;
            }
        }

        return Optional.of(attributes);
    }

    private void validate(@NonNull final String path) {
        if (!path.startsWith("/"))
            throw new RuntimeException("A path or template must start with /");
    }

    private boolean isAttribute(@NonNull final String str) {
        return str.startsWith("{") && str.endsWith("}");
    }

    @AllArgsConstructor
    class TemplateElement {
        final ElementType type;
        final String name;
    }

    enum ElementType {
        FIXED, ATTRIBUTE
    }
}
