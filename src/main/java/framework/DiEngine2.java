package framework;

import framework.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DiEngine2 {

    private static Map<Map<String, Class>, List<Field>> mappedQualifers=new HashMap<>();
    private static Map<Class,Object> instantietedSingletons=new HashMap<>();
    private static void buildDependencyContainer() { //TODO OVO SE PRVO DESAVA
        Set<Class> classes=ClassLoader.loadClasses();
        for(Class c:classes){
        Qualifier a= (Qualifier) c.getAnnotation(Qualifier.class);
        mappedQualifers.put(Map.of(a.value(),c),new ArrayList<>());
        for(Class i:c.getInterfaces()){
            System.out.println(i.getName());
            System.out.println("EVO GA OVDE");
        }

    }
    }

    public static void DependencyInjectionEngine(){
        Set<Class> classes=ClassLoader.loadClasses();
        System.out.println("KRECE ENGINE");
        for(Class c:classes){
            if(c.isAnnotationPresent(Controller.class) && !instantietedSingletons.containsKey(c)){

                buildDI(c);
            }


        }
    }
    public static Object buildDI(Class c){
        boolean hasAutowired=false;
        Object os=null;
        Map<Field,Object> autowiredObjects=new HashMap<>();

        for(Field field:c.getFields()){

            if(field.isAnnotationPresent(Autowired.class)){

                hasAutowired=true;
                autowiredObjects.put(field,buildDI(field.getType()));
            }
        }
        if(!hasAutowired){

            for(Annotation annotation:c.getAnnotations())
            {
                if(annotation instanceof Service ){
                    System.out.println("Service found");
                    try {
                        if(instantietedSingletons.containsKey(c))
                            os=instantietedSingletons.get(c);
                        else
                            os= c.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }else if(annotation instanceof Bean){
                 Bean  bean=(Bean) annotation;
                 if(bean.value().equals("Singleton")){
                        try {
                            if(instantietedSingletons.containsKey(c))
                                os=instantietedSingletons.get(c);
                            else
                                os= c.getDeclaredConstructor().newInstance();
                            instantietedSingletons.put(c,os);
                            System.out.println("SingletonBean found");
                        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                 }else{
                        try {
                            os= c.getDeclaredConstructor().newInstance();
                            System.out.println("DOSAO JE DOVDE?" +os);
                        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                                InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                 }
                }else if(annotation instanceof Component) {
                    try {

                        os=c.getDeclaredConstructor().newInstance();
                        System.out.println("NOT SINGLETON ALI NEW INSTANCE" + os);
                        return os;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return os;

        }else{
            System.out.println("class=   "+ c+" autowiredobjects=  " +autowiredObjects );
            for(Field f:autowiredObjects.keySet()){
                if(instantietedSingletons.containsKey(f.getDeclaringClass())) {
                    try {
                        f.set(instantietedSingletons.get(f.getDeclaringClass()),autowiredObjects.get(f));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else if (c.isAnnotationPresent(Service.class) ||c.isAnnotationPresent(Bean.class)) {
                    if(c.getAnnotation(Bean.class)!=null){
                        Bean bean= (Bean) c.getAnnotation(Bean.class);
                        if(bean.value().equals("Singleton")){
                            try {
                                 os= c.getDeclaredConstructor().newInstance();
                                instantietedSingletons.put(c,os);
                                f.set(os,autowiredObjects.get(f));
                            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                                    InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }else continue;
                    }else{
                        try {
                             os= c.getDeclaredConstructor().newInstance();
                            instantietedSingletons.put(c,os);
                            f.set(os,autowiredObjects.get(f));
                        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }



        System.err.println(os);
        return os;
    }




    public static void figureOutAutowired() { //OVO SE DRUGO DESAVA, NAKON buildDependancyContainer
        Set<Class> classes=ClassLoader.loadClasses();
        for (Class c : classes) {
            System.out.println(c.getName());
            for (Field field : c.getFields()) {
                if (field.isAnnotationPresent(framework.annotations.Autowired.class)) {
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        System.out.println("Qualifier found");
                        String val = field.getAnnotation(Qualifier.class).value();
                        Set<Map<String, Class>> keyset = mappedQualifers.keySet();
                        for (Map<String, Class> key : keyset) {
                            if (key.containsKey(val)) {

                                mappedQualifers.get(Map.of(val, key.get(val))).add(field);
                                System.out.println("PRONASAO QUALIFER SA" + val + " " + mappedQualifers);
                            }
                        }
                    } else { //TODO AKO NIJE QUALIFER PROVERI SE DA LI NIJE INTERFACE
                        Class classx = field.getType();
                        System.out.println("OVDE GLEDAM DA LI JE INTERFACE" + classx.getName());
                        if(classx.isInterface()){
                            System.err.println("INTERFACE BEZ QUALIFIERA"+ classx.getName() + " " + field.getName());
                        }else{
                            System.out.println("samo autowired sa klasom");
                            System.out.println("URADI DI" + classx.getName() + " " + field.getName());

                        }


                    }
                }

            }
        }
    }
}
