package alanisia.rpc.core.proxy;

import alanisia.rpc.core.config.Address;
import alanisia.rpc.core.config.ClientConfiguration;
import alanisia.rpc.core.future.FutureCommon;
import alanisia.rpc.core.future.RPCFuture;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.Client;
import alanisia.rpc.core.util.ZKUtil;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;
    private final String version;
    private static Client client = null;

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
        RPCFuture future = FutureCommon.request(request, connect2c(request));
        // RPCFuture future = FutureCommon.request(request, Client.getFuture());
        Object result = future.get();
        log.info("End to invoke method ^_^");
        if (result != null && client != null) {
            client.shutdown();
            client = null;
        }
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    private ChannelFuture connect2c(Request request) {
        String[] address = ZKUtil.discover(request.getClazz().getName() + "_" + request.getVersion()).split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);
        // todo
        if (!ClientConfiguration.contains())
        client = new Client(host, port);
        client.client();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        return ClientConfiguration.get(new Address(host, port));
    }
}
