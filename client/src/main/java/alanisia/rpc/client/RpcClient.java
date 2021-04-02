package alanisia.rpc.client;

import alanisia.rpc.api.HelloService;
import alanisia.rpc.core.client.Client;
import alanisia.rpc.core.proxy.ProxyInvocationHandler;
import alanisia.rpc.core.util.ZKUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.util.concurrent.TimeUnit;

@ComponentScan({"alanisia.rpc.core"})
@SpringBootApplication
public class RpcClient {
    private static final Logger log = LoggerFactory.getLogger(RpcClient.class);

    public static void main(String[] args) {
        SpringApplication.run(RpcClient.class, args);
        test();
    }

    public static void test() {
        Client client = new Client();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            ZKUtil.connect(ZKUtil.ZK_HOST, ZKUtil.ZK_PORT);
            client.client();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HelloService helloService = (HelloService) new ProxyInvocationHandler(HelloService.class, "1").newProxyInstance();
        HelloService helloService1 = (HelloService) new ProxyInvocationHandler(HelloService.class, "2").newProxyInstance();
        log.info("Say hello: {}", helloService.sayHello("Hello world"));
        log.info("1 + 2 = {}", helloService1.add(1, 2));
        client.shutdown();
    }

}
