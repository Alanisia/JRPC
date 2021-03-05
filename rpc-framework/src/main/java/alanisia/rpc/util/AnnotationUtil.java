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

    public static List<String> getValues(Annotation annotation) {
        String[] packages = (String[]) AnnotationUtils.getValue(annotation, "basePackage");
        if (packages != null) {
            log.info("{}", JsonUtil.toPrettyJson(packages));
            return new ArrayList<String>(Arrays.asList(packages));
        } else return null;
    }
}
