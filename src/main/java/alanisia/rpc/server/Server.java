package alanisia.rpc.server;

import alanisia.rpc.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Server {
    @Autowired private ServerHandler serverHandler;
    private final int port;

    public Server(int port) { this.port = port; }

    public void server() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {

                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                log.info("Server start at {}", port);
            } else {
                log.error("Server start failed, traces: {}", future.cause().getMessage());
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            System.exit(0);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
