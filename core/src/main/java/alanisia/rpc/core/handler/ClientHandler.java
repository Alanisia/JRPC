package alanisia.rpc.core.handler;

import alanisia.rpc.core.future.FutureCommon;
import alanisia.rpc.core.future.RPCFuture;
import alanisia.rpc.core.model.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientHandler extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) {
        log.info("Handler: Read from server");
        RPCFuture future = FutureCommon.get(response.getId());
        future.setResponse(response);
        FutureCommon.remove(response.getId());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}", cause.getMessage());
        ctx.close();
    }
}
