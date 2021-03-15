package alanisia.rpc.handler;

import alanisia.rpc.model.RPCFuture;
import alanisia.rpc.model.Request;
import alanisia.rpc.model.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Response> {
    private Map<Long, RPCFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) {
        log.info("Handler: Read from server");
        RPCFuture future = futureMap.get(response.getId());
        future.setResponse(response);
        futureMap.remove(response.getId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("Client handler -- channel active!");
    }

    public RPCFuture sendRequest(Request request, Channel channel) {
        RPCFuture future = new RPCFuture();
        futureMap.put(request.getId(), future);
        channel.writeAndFlush(request);
        return future;
    }
}
