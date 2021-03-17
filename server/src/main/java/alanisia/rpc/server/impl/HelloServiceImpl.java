package alanisia.rpc.server.impl;

import alanisia.rpc.core.annotation.RPC;
import alanisia.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RPC(value = HelloService.class, version = "1")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return name;
    }

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
