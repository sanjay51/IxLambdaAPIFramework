package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class AllowSelfPolicy implements Policy {
    final String userIdAttributeName;
    final String requesterIdAttributeName;

    @Override
    public boolean verify(final Map<String, Parameter> attributeMap) {
        final Parameter userId = attributeMap.get(userIdAttributeName);
        final Parameter requesterId = attributeMap.get(requesterIdAttributeName);

        return userId.getStringValue().equals(requesterId.getStringValue());
    }
}
