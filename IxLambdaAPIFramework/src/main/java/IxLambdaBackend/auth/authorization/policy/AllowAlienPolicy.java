package IxLambdaBackend.auth.authorization.policy;

import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.activity.ParameterMap;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class AllowAlienPolicy implements Policy {
    final String userIdAttributeName;
    final String requesterIdAttributeName;
    final String requesterAccessLevelAttributeName;
    final int minimumAccessLevel;

    @Override
    public boolean verify(final ParameterMap paramMap) {
        final Parameter userId = paramMap.get(userIdAttributeName);
        final Parameter requesterId = paramMap.get(requesterIdAttributeName);

        if (!userId.getStringValue().equals(requesterId.getStringValue())) {
            final int requesterAccessLevel = paramMap.get(requesterAccessLevelAttributeName).getIntValue();
            if (requesterAccessLevel >= minimumAccessLevel) return true;
        }

        return false;
    }
}
