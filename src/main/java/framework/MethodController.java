package framework;

import java.lang.invoke.ConstantBootstraps;
import java.lang.reflect.Method;

public class MethodController {
    private Method method;
    private Object controller;
    public MethodController(Object controller, Method method) {
        this.method = method;
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }

    @Override
    public String toString() {
        return "MethodController{" +
                "method=" + method.getName() +
                ", controller=" + controller +
                '}';
    }
}
