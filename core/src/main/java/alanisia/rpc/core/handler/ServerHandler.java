package alanisia.rpc.core.handler;

import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.model.Response;
import alanisia.rpc.core.util.JsonUtil;
import alanisia.rpc.core.util.constant.Constant;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Request> {
    /**
     * Read from channel.
     * Read the requests from client so I can deserialize them and call methods
     * @param ctx context
     * @param request request
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request request) {
        Object result = invokeMethod(request);
        Response response = new Response(request.getId(), request.getMethodName(), Constant.SUCCESS, result);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
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
