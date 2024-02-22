package framework;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.HashSet;
import java.util.Set;

public class ClassLoader {
    public static String packageName;
    public static Set<Class> loadClasses() {
       Reflections reflections=new Reflections(packageName, new SubTypesScanner(false));
       Set<Class> classes= new HashSet<>(reflections.getSubTypesOf(Object.class));
       return classes;
    }
}
