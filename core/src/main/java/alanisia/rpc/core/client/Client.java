package alanisia.rpc.core.client;

import alanisia.rpc.core.handler.ClientHandler;
import alanisia.rpc.core.handler.RPCDecoder;
import alanisia.rpc.core.handler.RPCEncoder;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final Bootstrap bootstrap = new Bootstrap();

    public void client() {
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new RPCEncoder(Request.class));
                        socketChannel.pipeline().addLast(new RPCDecoder(Response.class));
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
    }

    public static void connect(Address address) {
        try {
            ChannelFuture future = bootstrap.connect(address.getHost(), address.getPort()).sync();
            ClientCommon.put(address.getHost(), address.getPort(), future);
            log.info("Connect to server {}:{} successfully", address.getHost(), address.getPort());
            // no need to add finally block if we don't want to call
            // future.channel().closeFuture().sync()
            // so the channel won't be closed
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage());
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
    }
}
