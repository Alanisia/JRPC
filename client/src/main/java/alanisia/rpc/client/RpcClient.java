package alanisia.rpc.client;

import alanisia.rpc.api.HelloService;
import alanisia.rpc.core.proxy.ProxyInvocationHandler;
import alanisia.rpc.core.Client;
import alanisia.rpc.core.util.ZKUtil;
import alanisia.rpc.core.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

@Slf4j
@ComponentScan({"alanisia.rpc.core"})
@SpringBootApplication
public class RpcClient {
    public static void main(String[] args) {
        SpringApplication.run(RpcClient.class, args);
        test();
    }

    public static void test() {
//        Client client = new Client("localhost", Constant.SERVER_PORT);
//        client.client();
//        try {
//            TimeUnit.MILLISECONDS.sleep(1000);
//        } catch (InterruptedException ignored) { }
        try {
            ZKUtil.connect(ZKUtil.ZK_HOST, ZKUtil.ZK_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HelloService helloService = (HelloService) new ProxyInvocationHandler(HelloService.class, "1").newProxyInstance();
        HelloService helloService1 = (HelloService) new ProxyInvocationHandler(HelloService.class, "2").newProxyInstance();
        log.info("Say hello: {}", helloService.sayHello("Hello world"));
        log.info("1 + 2 = {}", helloService1.add(1, 2));
        // client.shutdown();
    }

}
