package alanisia.rpc.core.proxy;

import alanisia.rpc.core.annotation.RPC;
import alanisia.rpc.core.annotation.RPCScan;
import alanisia.rpc.core.util.AnnotationUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public class Proxy {
    private final Map<String, Class<?>> proxyMap;

    public Proxy() {
        this.proxyMap = new ConcurrentHashMap<>();
    }

    public void setApi(String name, Class<?> clazz) {
        proxyMap.put(name, clazz);
    }

    public void initProxyMap(Class<?> clazz) {
        // Get packages that with @RPCScan
        List<String> packages = AnnotationUtil.getValues(clazz, RPCScan.class, "value");
        if (packages != null) {
            for (String paquete : packages) {
                // Scan classes with @RPC under current package
                Set<Class<?>> classSet = AnnotationUtil.scan(paquete, RPC.class);
                for (Class<?> c : classSet) {
                    log.info("Class: {}", c.getName());
                    proxyMap.put(c.getName(), c);
                }
            }
        }
    }
}
