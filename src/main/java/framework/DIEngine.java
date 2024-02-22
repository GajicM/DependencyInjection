package framework;

import framework.annotations.Bean;
import framework.annotations.Controller;
import framework.annotations.Qualifier;
import framework.annotations.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DIEngine {
    public static Map<Class,Object> instantietedSingletons=new HashMap<>();
    public static Set<Class> classes=ClassLoader.loadClasses();
    public static Map<Map<String,Class>, List<Field>> mappedQualifers=new HashMap<>();
    public static List<Field> autowiredFields=new ArrayList<>();
    public static void instantiateSingletons() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    //    Set<Class> classes=ClassLoader.loadClasses("testapp");
        for(Class c:classes){
            Set<Annotation> annotations= Set.of( c.getAnnotations());

            for(Annotation a:annotations){
                if(a instanceof Controller){
                    System.out.println("Controller found");
                   Object os= c.getDeclaredConstructor().newInstance();
                   instantietedSingletons.put(c,os);
                }
                if(a instanceof Service){
                    System.out.println("Service found");
                    Object os= c.getDeclaredConstructor().newInstance();
                    instantietedSingletons.put(c,os);
                }
                if(a instanceof Bean){
                    Bean bean=(Bean) a;;
                    if(bean.value().equals("Singleton")){
                        Object os= c.getDeclaredConstructor().newInstance();
                        instantietedSingletons.put(c,os);
                        System.out.println("Service found");
                    }
                }
                if(a instanceof Qualifier){
                    buildDependencyContainer(c);
                }

            }
        }
    }

    private static void buildDependencyContainer(Class c) { //TODO OVO SE PRVO DESAVA
       Qualifier    a= (Qualifier) c.getAnnotation(Qualifier.class);
       mappedQualifers.put(Map.of(a.value(),c),new ArrayList<>());
        for(Class i:c.getInterfaces()){
            System.out.println(i.getName());
            System.out.println("EVO GA OVDE");
        }

    }

    public static void figureOutAutowired() { //OVO SE DRUGO DESAVA, NAKON buildDependancyContainer
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
                            autowiredFields.add(field);
                        }


                    }
                }

            }
        }
    }

    public static void doDependencyInjection(){
        for(Field f:autowiredFields) {
            Class tipPolja=f.getType();
            Class klasaPolja=f.getDeclaringClass();
            //ovaj deo je za autowired bez qualifiera, ali samo singletoni
            if(instantietedSingletons.containsKey(tipPolja)){
                if(instantietedSingletons.containsKey(klasaPolja)){
                    System.out.println("oba su singleton");
                try {
                    f.setAccessible(true);
                    f.set(instantietedSingletons.get(klasaPolja),instantietedSingletons.get(tipPolja));
                    f.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } } else{
                    System.out.println("onaj koji jeste autowired, ali njegova klasa nije singleton");
                    System.out.println("Ovo jedino u aspektu");

                }
                }else if(instantietedSingletons.containsKey(klasaPolja)){
                System.out.println("onaj koji jeste autowired, i njegova klasa je singleton, ali njegovo polje nije singleton nego componenta neka");;
                try {
                    f.set(instantietedSingletons.get(f.getDeclaringClass()),tipPolja.getConstructor().newInstance());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }else {
                System.out.println("oni koji su autowired, ali u njihova klasa nije singleton, a nije ni klasa polja");
            }
        }

    }




}
