package alanisia.rpc.client;

import alanisia.rpc.api.HelloService;
import alanisia.rpc.proxy.ProxyInvocationHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcClient {
    public static void main(String[] args) {
        SpringApplication.run(RpcClient.class, args);
        test();
    }

    public static void test() {
        HelloService helloService = (HelloService) new ProxyInvocationHandler(HelloService.class).newProxyInstance();
        helloService.sayHello("Hello world");
    }

}
