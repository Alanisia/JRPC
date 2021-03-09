package alanisia.rpc.util;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
public final class AnnotationUtil {
    /**
     * Scan all implementations with annotation "@RPC" of interfaces
     * @param packagePath path of package
     * @param annotation reflection of annotation
     * @return I don't know what should return, maybe a set
     */
    public static Set<Class<?>> scan(String packagePath, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packagePath);
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * Get values
     * @param clazz class that with annotation
     * @param a class of annotation
     * @param attribute attribute of annotation
     * @return a list that content package names in "basePackage"
     */
    public static List<String> getValues(Class<?> clazz, Class<? extends Annotation> a, String attribute) {
        Annotation annotation = clazz.getAnnotation(a);
        String[] packages = (String[]) AnnotationUtils.getValue(annotation, attribute);
        if (packages != null) {
            log.info("{}", JsonUtil.toJson(packages));
            return new ArrayList<>(Arrays.asList(packages));
        } else return null;
    }
}
