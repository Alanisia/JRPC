package alanisia.rpc.client;

import alanisia.rpc.api.HelloService;
import alanisia.rpc.proxy.ProxyInvocationHandler;
import alanisia.rpc.util.Client;
import alanisia.rpc.util.constant.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RpcClient {
    public static void main(String[] args) {
        SpringApplication.run(RpcClient.class, args);
        test();
    }

    public static void test() {
        new Client("localhost", Constant.SERVER_PORT).client();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException ignored) { }
        HelloService helloService = (HelloService) new ProxyInvocationHandler(HelloService.class).newProxyInstance();
        helloService.sayHello("Hello world");
    }

}
