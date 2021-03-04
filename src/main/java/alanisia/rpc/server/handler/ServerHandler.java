package alanisia.rpc.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Read from channel.
     * Read the requests from client so I should deserialize them and call methods
     * @param ctx context
     * @param msg message
     * @throws Exception who cares the exceptions
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}", cause.getMessage());
    }
}
