package alanisia.rpc.core.handler;

import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.model.Response;
import alanisia.rpc.core.proxy.Proxy;
import alanisia.rpc.core.util.constant.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Service
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
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static Object invokeMethod(Request request) {
        try {
            Class<?> clazz = request.getClazz();
            Method method = clazz.getMethod(request.getMethodName(), request.getParamTypes());
            String version = clazz.getName() + "_" + request.getVersion();
            if (Proxy.isClassExisted(version))
                return method.invoke(Proxy.getClazz(version).getDeclaredConstructor().newInstance(), request.getParams());
            else throw new NoSuchMethodException();
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
