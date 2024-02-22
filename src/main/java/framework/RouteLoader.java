package framework;

import framework.annotations.Controller;
import framework.annotations.GET;
import framework.annotations.POST;
import framework.annotations.Path;
import framework.request.Route;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RouteLoader {
    public static Map<Route,MethodController> mappedRoutes=new HashMap<>();

    public static  Map<Route,MethodController> loadRoutes(){
        Set<Class> classes=ClassLoader.loadClasses();

        for(Class c:classes){
            Set<Annotation> annotations= Set.of( c.getAnnotations());
    //        System.out.println(c+ " "+annotations);
           for(Annotation a:annotations){
                if(a instanceof Controller){
            //        System.out.println("Controller found");
                    loadMethodControllers(c);

                }

            }
        }
        System.out.println(mappedRoutes);
        return mappedRoutes;
    }

    private static void loadMethodControllers(Class c) {
        Set<MethodController> methodControllers=new HashSet<>();
        Object controllerObject=null;
       for(Method m:c.getMethods()){
          Set<Annotation> annotations= Set.of( m.getAnnotations());
           String path=null;
           framework.request.enums.Method method=null;
           for(Annotation a:annotations){
              if(a instanceof Path){
                  Path p=(Path) a;
                 path=p.value();
              }
                if(a instanceof GET){
                    method= framework.request.enums.Method.GET;
                }
                 if(a instanceof POST){
                     method=framework.request.enums.Method.POST;
                 }
           }
           if (path==null || method==null){
               continue;
           }else{
                Route r=new Route(path,method);
                for(Object o:DIENGINE3.instantiatedControllers){
                    if(o.getClass().equals(c)){
                        controllerObject=o;
                    }
                }
                   mappedRoutes.put(r,new MethodController(controllerObject,m));
               }






       }

    }

}
