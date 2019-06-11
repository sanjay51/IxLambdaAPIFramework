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
import java.util.Set;

public abstract class LambdaRestService implements RequestHandler<Request, Response> {

    static final Gson gson = new Gson();
    final Set<Method> getMethods;
    final Set<Method> postMethods;

    public LambdaRestService() {
        final String packageName = this.getClass().getPackage().getName();
        final MethodAnnotationsScanner packageScanner = new MethodAnnotationsScanner();

        this.getMethods = new Reflections(packageName, packageScanner).getMethodsAnnotatedWith(GET.class);
        this.postMethods = new Reflections(packageName, packageScanner).getMethodsAnnotatedWith(POST.class);
    }

    @Override
    public Response handleRequest(final Request request, final Context context) {
        final HttpMethod httpMethod = request.getContext().getHttpMethod();

        if (httpMethod == HttpMethod.GET) {
            try {
                return this.invokeGetMethod(request);
            } catch (final Exception e) {
                throw new InternalError(e);
            }
        }

        throw new InternalException("Something bad happened");
    }

    private Response invokeGetMethod(final Request request) throws Exception {
        try {
            final Activity activity = (Activity) this.getMethods.iterator().next().invoke(this);
            activity.setRequest(request);
            return activity.getResponse();
        } catch (IllegalAccessException|InvocationTargetException e) {
            throw new UnknownOperationException("Something bad happened" + e);
        }
    }
}
