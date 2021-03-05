package alanisia.rpc.server.api.impl;

import alanisia.rpc.server.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
