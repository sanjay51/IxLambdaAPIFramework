package IxLambdaBackend.service;

import IxLambdaBackend.activity.Activity;
import IxLambdaBackend.annotation.GET;
import IxLambdaBackend.annotation.POST;
import IxLambdaBackend.exception.InternalException;
import IxLambdaBackend.exception.UnknownOperationException;
import IxLambdaBackend.request.Request;
import IxLambdaBackend.response.Response;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

public abstract class LambdaRestService implements RequestHandler<Request, Response> {

    static final Gson gson = new Gson();
    static boolean isInitialized = false;

    static MethodResolver methodResolver;

    public LambdaRestService() {
        init();
    }

    void init() {
        if (isInitialized) return;

        final String packageName = getClass().getPackage().getName();
        final MethodAnnotationsScanner packageScanner = new MethodAnnotationsScanner();

        final Set<Method> getMethods = new Reflections(packageName, packageScanner).getMethodsAnnotatedWith(GET.class);
        final Set<Method> postMethods = new Reflections(packageName, packageScanner).getMethodsAnnotatedWith(POST.class);

        methodResolver = new MethodResolver(getMethods, postMethods);
        methodResolver.init();

        isInitialized = true;
    }

    @Override
    public Response handleRequest(final Request request, final Context context) {
        final HttpMethod httpMethod = HttpMethod.valueOf(request.getHttpMethod());

        if (httpMethod == HttpMethod.GET) {
            try {
                return this.serveRequest(request);
            } catch (final Exception e) {
                throw new InternalError(e);
            }
        }

        throw new InternalException("Something bad happened");
    }

    private Response serveRequest(final Request request) throws Exception {
        try {
            System.out.println(gson.toJson(request));

            final Optional<MethodResolver.MethodResponse> methodResponse = methodResolver.resolve(request.getPath());
            if (!methodResponse.isPresent()) throw new InternalException("Route not defined");

            final Method method = methodResponse.get().getMethod();

            System.out.println(methodResponse);
            System.out.println(method.getReturnType());

            // If it's activity, handle activity
            if (method.getReturnType() == Activity.class) {
                final Activity activity = (Activity) method.invoke(this);
                activity.setRequest(request);
                activity.setPathParameters(methodResponse.get().getPathParameters());

                return activity.getResponse();
            }

            // Check if can pass request object
            if (method.getParameterCount() > 0 && method.getParameters()[0].getType() == Request.class) {
                return new Response(method.invoke(this, request));
            }

            // Otherwise, just invoke the method directly
            return new Response(method.invoke(this));

        } catch (IllegalAccessException|InvocationTargetException e) {
            throw new UnknownOperationException("Something bad happened" + e);
        }
    }
}
