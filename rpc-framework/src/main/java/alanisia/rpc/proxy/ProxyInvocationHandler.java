package alanisia.rpc.proxy;

import alanisia.rpc.model.Request;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class ProxyInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        log.info("Invoke method {}...", method.getName());
        Request request = new Request();
        request.setId(UUID.randomUUID().timestamp());
        request.setClazz(clazz);
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(objects);
        // TODO: sent request and get result of response
 
        Object result = null;
        log.info("End to invoke method ^_^");
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
}
