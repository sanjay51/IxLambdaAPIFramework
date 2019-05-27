package IxDeclarativeAPI.auth.authorization.policy;

import IxDeclarativeAPI.activity.Parameter;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class AllowAlienPolicy implements Policy {
    final String userIdAttributeName;
    final String requesterIdAttributeName;
    final String requesterAccessLevelAttributeName;
    final int minimumAccessLevel;

    @Override
    public boolean verify(final Map<String, Parameter> paramMap) {
        final Parameter userId = paramMap.get(userIdAttributeName);
        final Parameter requesterId = paramMap.get(requesterIdAttributeName);

        if (!userId.getStringValue().equals(requesterId.getStringValue())) {
            final int requesterAccessLevel = paramMap.get(requesterAccessLevelAttributeName).getIntValue();
            if (requesterAccessLevel >= minimumAccessLevel) return true;
        }

        return false;
    }
}
