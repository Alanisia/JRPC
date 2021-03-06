package alanisia.rpc.handler;

import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import alanisia.rpc.util.JsonUtil;
import alanisia.rpc.util.constant.Constant;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Service
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Read from channel.
     * Read the requests from client so I can deserialize them and call methods
     * @param ctx context
     * @param msg message
     * @throws Exception who cares the exceptions
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        log.info("Output: {}", JsonUtil.toPrettyJson(request));
        Object result = invokeMethod(request);
        Response response = new Response(request.getId(), request.getMethodName(), Constant.SUCCESS, result);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
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
