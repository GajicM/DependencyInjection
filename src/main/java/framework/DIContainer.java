package framework;

import framework.annotations.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DIContainer {

    public static Map<String,Class> qualifierMap = new HashMap<>();

    public static void loadQualifiers(){
        Set<Class> classes=ClassLoader.loadClasses();
        for(Class c:classes){
            if(c.isAnnotationPresent(Qualifier.class)){
                Qualifier qualifier=(Qualifier) c.getAnnotation(Qualifier.class);

                if(qualifierMap.containsKey(qualifier.value())){
                    throw new RuntimeException("There's already a bean with name: " + qualifier.value());
                }
                qualifierMap.put(qualifier.value(),c);
            }
        }

    }

}
