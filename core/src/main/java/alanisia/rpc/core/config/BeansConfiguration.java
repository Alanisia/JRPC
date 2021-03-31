package alanisia.rpc.core.config;

import alanisia.rpc.core.handler.RPCDecoder;
import alanisia.rpc.core.handler.RPCEncoder;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeansConfiguration {
    @Bean(name = "RPCRequestEncoder")
    public RPCEncoder rpcRequestEncoder() {
        log.info("Register bean RPCRequestEncoder");
        return new RPCEncoder(Request.class);
    }

    @Bean(name = "RPCRequestDecoder")
    public RPCDecoder rpcRequestDecoder() {
        log.info("Register bean RPCRequestDecoder");
        return new RPCDecoder(Request.class);
    }

    @Bean(name = "RPCResponseEncoder")
    public RPCEncoder rpcResponseEncoder() {
        log.info("Register bean RPCResponseEncoder");
        return new RPCEncoder(Response.class);
    }

    @Bean(name = "RPCResponseDecoder")
    public RPCDecoder rpcResponseDecoder() {
        log.info("Register bean RPCResponseDecoder");
        return new RPCDecoder(Response.class);
    }
}
