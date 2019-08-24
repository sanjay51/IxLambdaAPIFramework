package IxLambdaBackend.functional;

import IxLambdaBackend.ActivityFactory;
import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.activity.Parameter;
import IxLambdaBackend.auth.authorization.Authorization;
import IxLambdaBackend.auth.authorization.policy.*;
import IxLambdaBackend.exception.NotAuthorizedException;
import IxLambdaBackend.request.Request;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static IxLambdaBackend.TestData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthorizationTest {

    @Test
    public void testSelfAccessBasedAuthorization() {
        final Policy selfAllowPolicy = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);

        final Authorization authorizationStrategy = new Authorization(selfAllowPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }

    @Test
    public void testAlienAccessBasedAuthorization() {
        final Policy allowAlienPolicy = new AllowAlienPolicy(PARAM_userId, PARAM_requesterId, PARAM_requesterAccessLevel, 6);

        final Authorization authorizationStrategy = new Authorization(allowAlienPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue("blah"));
        activity.addParameter((new Parameter(PARAM_requesterAccessLevel, null)).withValue(10));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }

    @Test
    public void testAlienAccessWithLevelSameAsMinimum() {
        final Policy allowAlienPolicy = new AllowAlienPolicy(PARAM_userId, PARAM_requesterId, PARAM_requesterAccessLevel, 6);

        final Authorization authorizationStrategy = new Authorization(allowAlienPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue("blah"));
        activity.addParameter((new Parameter(PARAM_requesterAccessLevel, null)).withValue(6));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }

    @Test
    public void assertExceptionOnAuthorizationFailure() {
        final Policy allowAlienPolicy = new AllowAlienPolicy(PARAM_userId, PARAM_requesterId, PARAM_requesterAccessLevel, 6);

        final Authorization authorizationStrategy = new Authorization(allowAlienPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue("blah"));
        activity.addParameter((new Parameter(PARAM_requesterAccessLevel, null)).withValue(4));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertThrows(NotAuthorizedException.class, () -> activity.getResponse());
    }

    @Test
    public void assertExceptionOnAuthorizationFailureWithDenyAllPolicy() {
        final Policy denyAllPolicy = new DenyAllPolicy();

        final Authorization authorizationStrategy = new Authorization(denyAllPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.getAuthStrategies().add(authorizationStrategy);

        assertThrows(NotAuthorizedException.class, () -> activity.getResponse());
    }

    @Test
    public void testORPolicyPositiveExample() {
        final Policy selfAllowPolicy = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);
        final Policy denyAllPolicy = new DenyAllPolicy();
        final Policy firstOrSecondPolicy = new OR(Arrays.asList(selfAllowPolicy, denyAllPolicy));

        final Authorization authorizationStrategy = new Authorization(firstOrSecondPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }

    @Test
    public void testANDPolicyNegativeExample() {
        final Policy selfAllowPolicy = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);
        final Policy denyAllPolicy = new DenyAllPolicy();
        final Policy firstAndSecondPolicy = new AND(Arrays.asList(selfAllowPolicy, denyAllPolicy));

        final Authorization authorizationStrategy = new Authorization(firstAndSecondPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertThrows(NotAuthorizedException.class, () -> activity.getResponse());
    }

    @Test
    public void testORPolicyNegativeExample() {
        final Policy denyAllPolicy = new DenyAllPolicy();
        final Policy denyAllPolicy2 = new DenyAllPolicy();
        final Policy firstOrSecondPolicy = new OR(Arrays.asList(denyAllPolicy, denyAllPolicy2));

        final Authorization authorizationStrategy = new Authorization(firstOrSecondPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertThrows(NotAuthorizedException.class, () -> activity.getResponse());
    }

    @Test
    public void testANDPolicyPositiveExample() {
        final Policy selfAllowPolicy = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);
        final Policy selfAllowPolicy2 = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);
        final Policy firstOrSecondPolicy = new AND(Arrays.asList(selfAllowPolicy, selfAllowPolicy2));

        final Authorization authorizationStrategy = new Authorization(firstOrSecondPolicy);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }
}
