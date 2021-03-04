package alanisia.rpc.handler;

import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import alanisia.rpc.serialize.Serializer;
import alanisia.rpc.util.JsonUtil;
import alanisia.rpc.util.constant.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
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
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        Request request = Serializer.deserialize(bytes, Request.class);
        log.info("Output: {}", JsonUtil.toPrettyJson(request));

        // TODO: We get request. Then we should invoke method that we find in registry table.
        //  We will get result, so we can wrap a response object, serialize it to send to client.
        Object result = null; // temporarily
        Response response = new Response(request.getId(), request.getMethodName(), Constant.SUCCESS, result);
        byte[] responseBytes = Serializer.serialize(response);
        ctx.writeAndFlush(responseBytes).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}", cause.getMessage());
        ctx.close();
    }
}
