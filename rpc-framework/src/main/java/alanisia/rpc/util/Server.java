package alanisia.rpc.util;

import alanisia.rpc.handler.RPCDecoder;
import alanisia.rpc.handler.RPCEncoder;
import alanisia.rpc.handler.ServerHandler;
import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import alanisia.rpc.util.constant.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Server {
    private final int port;

    public Server(int port) { this.port = port; }

    public void server() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, Constant.SO_BACKLOG)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new RPCEncoder(Response.class));
                            ch.pipeline().addLast(new RPCDecoder(Request.class));
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                log.info("Server start at port {}", port);
                future.channel().closeFuture().sync();
            } else {
                log.error("Server start failed, traces: {}", future.cause().getMessage());
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            System.exit(Constant.FAILED);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
