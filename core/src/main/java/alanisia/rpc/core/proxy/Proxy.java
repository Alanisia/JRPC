package alanisia.rpc.core.proxy;

import alanisia.rpc.core.annotation.RPC;
import alanisia.rpc.core.annotation.RPCScan;
import alanisia.rpc.core.util.AnnotationUtil;
import alanisia.rpc.core.util.constant.Constant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public class Proxy {
    private static Map<String, Class<?>> proxyMap = new ConcurrentHashMap<>();;

    private Proxy() { }

    public static boolean isClassExisted(String version) {
        return proxyMap.containsKey(version);
    }

    public static Class<?> getClazz(String version) { return proxyMap.get(version); }

    public static void initProxyMap(Class<?> clazz) {
        // Get packages that with @RPCScan
        List<String> packages = AnnotationUtil.getValues(clazz, RPCScan.class, Constant.RPC_VALUE);
        if (packages != null) {
            for (String paquete : packages) {
                // Scan classes with @RPC under current package
                Set<Class<?>> classSet = AnnotationUtil.scan(paquete, RPC.class);
                for (Class<?> c : classSet) {
                    Class<?> value = (Class<?>) AnnotationUtil.getValue(c, RPC.class, Constant.RPC_VALUE);
                    String version = (String) AnnotationUtil.getValue(c, RPC.class, Constant.RPC_VERSION);
                    if (value != null && version != null) {
                        String key = value + "_" + version;
                        proxyMap.put(key, c);
                    }
                }
            }
        }
    }

    public static void print() {
        for (Map.Entry<String, Class<?>> entry : proxyMap.entrySet()) {
            log.info("{} : {}", entry.getKey(), entry.getValue().getName());
        }
    }
}
