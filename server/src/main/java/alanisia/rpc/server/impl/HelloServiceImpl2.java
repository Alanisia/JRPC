package alanisia.rpc.server.impl;

import alanisia.rpc.core.annotation.RPC;
import alanisia.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RPC(HelloService.class)
public class HelloServiceImpl2 implements HelloService {
    @Override
    public void sayHello(String name) {
        log.info("Say name: {}", name);
    }

    @Override
    public int add(int a, int b) {
        return a + b * 10;
    }
}
