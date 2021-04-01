package alanisia.rpc.core.proxy;

import alanisia.rpc.core.client.Address;
import alanisia.rpc.core.client.Client;
import alanisia.rpc.core.client.ClientCommon;
import alanisia.rpc.core.future.FutureCommon;
import alanisia.rpc.core.future.RPCFuture;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.util.ZKUtil;
import alanisia.rpc.core.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
        log.info("Invoke method {}...", method.getName());
        Request request = new Request();
        request.setId(System.currentTimeMillis());
        request.setClazz(clazz);
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(objects);
        request.setVersion(version);
        Address address = getAddress(request);
        Client.connect(address);
        RPCFuture future = FutureCommon.request(request, ClientCommon.get(address));
        Object result = future.get();
        log.info("End to invoke method ^_^");
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    public Address getAddress(Request request) {
        String addr = ZKUtil.discover(request.getClazz().getName() + "_" + request.getVersion());
        if (addr == null) {
            log.error("No api.");
            System.exit(Constant.FAILED);
        }
        String[] address = addr.split(":");
        return new Address(address[0], Integer.parseInt(address[1]));
    }
}
