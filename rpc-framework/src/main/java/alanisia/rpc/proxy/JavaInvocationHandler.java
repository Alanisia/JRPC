package alanisia.rpc.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@AllArgsConstructor
public class JavaInvocationHandler implements InvocationHandler {
    private final Object object;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        log.info("Invoke method {}...", method.getName());
        Object result = method.invoke(object, objects);
        log.info("End to invoke method ^_^");
        return result;
    }

    public Object newProxyInstance() {
        Class<?> clazz = object.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

}
