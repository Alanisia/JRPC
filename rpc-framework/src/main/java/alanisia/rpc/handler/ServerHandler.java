package alanisia.rpc.handler;

import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import alanisia.rpc.util.JsonUtil;
import alanisia.rpc.util.constant.Constant;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Read from channel.
     * Read the requests from client so I can deserialize them and call methods
     * @param ctx context
     * @param msg message
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Request request = (Request) msg;
        log.info("Output: {}", JsonUtil.toPrettyJson(request));
        Object result = invokeMethod(request);
        Response response = new Response(request.getId(), request.getMethodName(), Constant.SUCCESS, result);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel - active");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("channel - read complete");
        ctx.flush();
    }

    private static Object invokeMethod(Request request) {
        try {
            Method method = request.getClazz().getMethod(request.getMethodName(), request.getParamTypes());
            return method.invoke(request, request.getParams());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }
}
