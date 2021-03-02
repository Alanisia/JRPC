package alanisia.rpc.net;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Demo {
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private EventLoopGroup bossGroup = new NioEventLoopGroup();


}
