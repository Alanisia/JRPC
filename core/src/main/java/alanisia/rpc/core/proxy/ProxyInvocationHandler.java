package alanisia.rpc.core.proxy;

import alanisia.rpc.core.future.FutureCommon;
import alanisia.rpc.core.handler.ClientHandler;
import alanisia.rpc.core.future.RPCFuture;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.Client;
import alanisia.rpc.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;
    private final String version;

    public ProxyInvocationHandler(Class<?> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) {
        // register centre
//        String hostYPort = ZKUtil.getDataFromServer(method.getName());
//        if (hostYPort == null || hostYPort.length() == 0) return null;
//        String[] content = hostYPort.split(":");
        log.info("Invoke method {}...", method.getName());
        Request request = new Request();
        request.setId(System.currentTimeMillis());
        request.setClazz(clazz);
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(objects);
        request.setVersion(version);
        RPCFuture future = FutureCommon.request(request, Client.getFuture());
        Object result = future.get();
        log.info("End to invoke method ^_^");
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
