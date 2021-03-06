package alanisia.rpc.server.impl;

import alanisia.rpc.annotation.RPC;
import alanisia.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RPC(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        log.info("{}", name);
    }

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
