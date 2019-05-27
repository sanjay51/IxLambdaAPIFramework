package IxDeclarativeAPI.activity;

import IxDeclarativeAPI.ActivityFactory;
import IxDeclarativeAPI.auth.authorization.Authorization;
import IxDeclarativeAPI.auth.authorization.policy.AND;
import IxDeclarativeAPI.auth.authorization.policy.AllowAlienPolicy;
import IxDeclarativeAPI.auth.authorization.policy.AllowSelfPolicy;
import IxDeclarativeAPI.auth.authorization.policy.DenyAllPolicy;
import IxDeclarativeAPI.auth.authorization.policy.OR;
import IxDeclarativeAPI.auth.authorization.policy.Policy;
import IxDeclarativeAPI.exception.NotAuthorizedException;
import IxDeclarativeAPI.request.Request;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static IxDeclarativeAPI.TestData.API_getUserById;
import static IxDeclarativeAPI.TestData.PARAM_requesterAccessLevel;
import static IxDeclarativeAPI.TestData.PARAM_requesterId;
import static IxDeclarativeAPI.TestData.PARAM_userId;
import static IxDeclarativeAPI.TestData.TEST_USER_ID;
import static IxDeclarativeAPI.TestData.getSampleRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActivityTest {

    @Test
    public void testSelfAccessBasedAuthorization() {
        final Policy selfAllowPolicy = new AllowSelfPolicy(PARAM_userId, PARAM_requesterId);
        final List<Policy> policies = Arrays.asList(selfAllowPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }

    @Test
    public void testAlienAccessBasedAuthorization() {
        final Policy allowAlienPolicy = new AllowAlienPolicy(PARAM_userId, PARAM_requesterId, PARAM_requesterAccessLevel, 6);
        final List<Policy> policies = Arrays.asList(allowAlienPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(allowAlienPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(allowAlienPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(denyAllPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(firstOrSecondPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(firstAndSecondPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(firstOrSecondPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

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
        final List<Policy> policies = Arrays.asList(firstOrSecondPolicy);

        final Authorization authorizationStrategy = new Authorization(policies);

        final Request request = getSampleRequest();

        final Activity activity = ActivityFactory.newInstance(API_getUserById, request);
        activity.addParameter((new Parameter(PARAM_requesterId, null)).withValue(TEST_USER_ID));
        activity.getAuthStrategies().add(authorizationStrategy);

        assertDoesNotThrow(() -> activity.getResponse());
    }
}
