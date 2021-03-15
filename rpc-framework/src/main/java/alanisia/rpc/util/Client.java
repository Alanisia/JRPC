package alanisia.rpc.util;

import alanisia.rpc.handler.ClientHandler;
import alanisia.rpc.handler.RPCDecoder;
import alanisia.rpc.handler.RPCEncoder;
import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private static Channel channel = null;
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
                            socketChannel.pipeline().addLast(new RPCDecoder(Response.class));
                            socketChannel.pipeline().addLast(new ClientHandler());
                            socketChannel.pipeline().addLast(new RPCEncoder(Request.class));
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            future.addListener(f -> {
                if (f.isSuccess()) {
                    log.info("Connect to server {}:{} successfully", host, port);
                } else {
                    log.error("Failed to connect server, error caused: {}", f.cause().getMessage());
                    bossGroup.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    public static Channel getChannel() {
        return channel;
    }
}
