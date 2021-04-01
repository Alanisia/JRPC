package alanisia.rpc.core.proxy;

import alanisia.rpc.core.client.Address;
import alanisia.rpc.core.client.Client;
import alanisia.rpc.core.client.ClientCommon;
import alanisia.rpc.core.future.FutureCommon;
import alanisia.rpc.core.future.RPCFuture;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.util.ZKUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class ProxyInvocationHandler implements InvocationHandler {
    private final Class<?> clazz;
    private final String version;
    private Client client = null;

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
        connect2c(request);
        RPCFuture future = FutureCommon.request(request, client.getFuture());
        // RPCFuture future = FutureCommon.request(request, Client.getFuture());
        Object result = future.get();
        log.info("End to invoke method ^_^");
//        if (result != null && client != null) {
//            client.shutdown();
//            client = null;
//        }
        return result;
    }

    public Object newProxyInstance() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    private void connect2c(Request request) {
        String[] address = ZKUtil.discover(request.getClazz().getName() + "_" + request.getVersion()).split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);
        Address addr = new Address(host, port);
        if (!ClientCommon.contains(addr)) {
            log.info("Generate a new client");
            client = new Client(host, port);
            ClientCommon.put(host, port, client);
            client.client();
        }
//        } else {
//            log.info("Get existed client from client map");
//            client = ClientCommon.get(addr);
//        }
//        // executorService.execute(() -> client.client());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

//    public ExecutorService getExecutorService() {
//        return executorService;
//    }
}
