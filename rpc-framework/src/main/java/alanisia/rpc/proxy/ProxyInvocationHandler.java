package alanisia.rpc.proxy;

import alanisia.rpc.handler.ClientHandler;
import alanisia.rpc.model.RPCFuture;
import alanisia.rpc.model.Request;
import alanisia.rpc.util.ZKUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Slf4j
public class ProxyInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;

    @Autowired
    private ClientHandler clientHandler;

    public ProxyInvocationHandler(Class<?> clazz) { this.clazz = clazz; }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        // register centre
//        String hostYPort = ZKUtil.getDataFromServer(method.getName());
//        if (hostYPort == null || hostYPort.length() == 0) return null;
//        String[] content = hostYPort.split(":");
        // todo: get handler

        // no register center

        // todo: call client handler
        //  cuz there are several services so we define a map to save handlers corresponding to each service

        log.info("Invoke method {}...", method.getName());
        Request request = new Request();
        request.setId(System.currentTimeMillis());
        request.setClazz(clazz);
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(objects);
        RPCFuture future = clientHandler.sendRequest(request, null);
        Object result = future.get();
        log.info("End to invoke method ^_^");
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
