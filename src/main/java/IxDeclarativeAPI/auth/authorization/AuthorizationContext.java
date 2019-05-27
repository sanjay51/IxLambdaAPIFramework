package IxDeclarativeAPI.auth.authorization;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class AuthorizationContext {
    private Map<String, AccessLevel> resourceAccessMap;
}
