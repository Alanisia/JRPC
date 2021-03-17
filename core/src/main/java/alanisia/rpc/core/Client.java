package alanisia.rpc.core;

import alanisia.rpc.core.handler.ClientHandler;
import alanisia.rpc.core.handler.RPCDecoder;
import alanisia.rpc.core.handler.RPCEncoder;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private static ChannelFuture future = null;
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void client() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
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
            future = bootstrap.connect(host, port).sync();
            log.info("Connect to server {}:{} successfully", host, port);
            // no need to add finally block if we don't want to call
            // future.channel().closeFuture().sync()
            // so the channel won't be closed
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage());
        }
    }

    public static ChannelFuture getFuture() {
        return future;
    }
}