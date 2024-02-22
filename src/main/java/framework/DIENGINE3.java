package framework;

import framework.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static framework.DIEngine.classes;

public class DIENGINE3 {
        public static Set<Object> instantiatedControllers=new HashSet<>();
        public static Map<Class,Object> instantiatedSingletons=new HashMap<>();
        public static void doAll(){
            initiateSingletonMap();
            DIContainer.loadQualifiers();
            try {
                instantiateControllers();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            instatiateControllerDependancies();
            printClasses();
        }
        public static void initiateSingletonMap(){
            for(Class c:classes){
                Set<Annotation> annotations= Set.of( c.getAnnotations());

                for(Annotation a:annotations){
                    if(a instanceof Controller){
                        instantiatedSingletons.put(c,null);
                    }
                    if(a instanceof Service){
                        instantiatedSingletons.put(c,null);
                    }
                    if(a instanceof Bean){
                        Bean bean=(Bean) a;;
                        if(bean.value().equals("Singleton")){

                            instantiatedSingletons.put(c,null);

                        }
                    }
                    if(a instanceof Qualifier){
                    //    buildDependencyContainer(c);

                    }

                }
            }
        }

    public static void instantiateControllers() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Set<Class> classes=ClassLoader.loadClasses();
        for(Class c:classes){
            if(c.isAnnotationPresent(Controller.class)){

                Object os= c.getDeclaredConstructor().newInstance();
                instantiatedControllers.add(os);
                instantiatedSingletons.put(c,os);
            }
            }
    }
    public static void instatiateControllerDependancies(){
        for(Object o:instantiatedControllers){
            try {
                injectDependencies(o);

            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void injectDependencies(Object c) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Field[] fields = c.getClass().getDeclaredFields();
        for (Field f : fields) {




            if (f.isAnnotationPresent(Autowired.class)  &&
                    (f.isAnnotationPresent(Qualifier.class) ||f.getType().isAnnotationPresent(Service.class) || f.getType().isAnnotationPresent(Component.class)
                            || f.getType().isAnnotationPresent(Bean.class))) {
                instantiateField(f, c);

            }
            if(f.getType().isInterface() && !f.isAnnotationPresent(Qualifier.class) && f.isAnnotationPresent(Autowired.class)){
                        System.err.println("Field is Autowired and not Qualifier but is interface " + f);
                    }
                   if(f.isAnnotationPresent(Autowired.class)  && !f.getType().isInterface() && !(f.getType().isAnnotationPresent(Service.class) || f.getType().isAnnotationPresent(Component.class)
                        ||f.getType().isAnnotationPresent(Controller.class)    || f.getType().isAnnotationPresent(Bean.class) )){

                        System.err.println("Field is Autowired and not Bean " + f.getName() + f.getDeclaringClass());
                    }



        }
    }

    private static void instantiateField(Field f, Object object) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
       f.setAccessible(true);
     Object   instantiatedObject=null;
       boolean isVerbose=f.getAnnotation(Autowired.class).verbose();
        if(f.getType().isInterface()){
            //TODO do qualifiers
            if( f.isAnnotationPresent(Qualifier.class)){
               String qualifierValue= f.getAnnotation(Qualifier.class).value();
                Class cx=DIContainer.qualifierMap.get(qualifierValue);
                if(cx!=null &&instantiatedSingletons.containsKey(cx)){
                    if(instantiatedSingletons.get(cx)==null){
                         instantiatedObject=DIContainer.qualifierMap.get(qualifierValue).getDeclaredConstructor().newInstance();
                        instantiatedSingletons.put(cx,instantiatedObject);
                        if(isVerbose){
                            System.out.println("Initialized "+f.getType() + " " + f.getName()+ " in "
                                    + f.getDeclaringClass() +" on " + LocalDate.now() + "  " +instantiatedObject);
                        };
                        injectDependencies(instantiatedObject);
                    }
                    f.set(object,instantiatedSingletons.get(cx));
                }else {
                     instantiatedObject=DIContainer.qualifierMap.get(qualifierValue).getDeclaredConstructor().newInstance();
                    if(isVerbose){
                        System.out.println("Initialized "+f.getType() + " " + f.getName()+ " in "
                                + f.getDeclaringClass() +" on " + LocalDate.now() + "  " +instantiatedObject);
                    };
                    injectDependencies(instantiatedObject);
                    f.set(object,instantiatedObject);
                }
             //   Object instantiatedObject=DIContainer.qualifierMap.get(f.getAnnotation(Qualifier.class).value()).getDeclaredConstructor().newInstance();
            }
  //          System.out.println("ULAZI OVDE "+f);
        }else
        {
            if(instantiatedSingletons.keySet().contains(f.getType())){
                if(instantiatedSingletons.get(f.getType())==null){
                     instantiatedObject=f.getType().getDeclaredConstructor().newInstance();
          //          System.out.println(instantiatedObject);
                    instantiatedSingletons.put(f.getType(),instantiatedObject);
                    if(isVerbose){
                        System.out.println("Initialized "+f.getType() + " " + f.getName()+ " in "
                                + f.getDeclaringClass() +" on " + LocalDate.now() + "  " +instantiatedObject);
                    };
                    injectDependencies(instantiatedObject);

                }
                f.set(object,instantiatedSingletons.get(f.getType()));
            }else {
                 instantiatedObject = f.getType().getDeclaredConstructor().newInstance();
                if(isVerbose){
                    System.out.println("Initialized "+f.getType() + " " + f.getName()+ " in "
                            + f.getDeclaringClass() +" on " + LocalDate.now() + "  " +instantiatedObject);
                };
                injectDependencies(instantiatedObject);
                f.set(object, instantiatedObject);
            }
        }

    }



    public static void printClasses(){
        System.out.println("pocetak");
        for(Object c:instantiatedSingletons.values()){
            System.out.println(c);
        }
        System.out.println("kraj");
    }

}




