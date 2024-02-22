package framework.request;

import framework.request.enums.Method;

import java.util.Objects;

public class Route {
    private String path;
    private Method method;

    public Route(String path, Method method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return this.path;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Route{" +
                "path='" + path + '\'' +
                ", method=" + method +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
         return this.path.equals(((Route)obj).getPath()) && this.method.equals(((Route)obj).getMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
