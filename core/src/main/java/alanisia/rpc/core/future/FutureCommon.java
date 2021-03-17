package alanisia.rpc.core.future;

import alanisia.rpc.core.model.Request;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FutureCommon {
    private static Map<Long, RPCFuture> futureMap = new ConcurrentHashMap<>();

    private FutureCommon() {}

    public static void put(Long key, RPCFuture value) {
        futureMap.put(key, value);
    }

    public static RPCFuture get(Long key) {
        return futureMap.get(key);
    }

    public static void remove(Long key) {
        futureMap.remove(key);
    }

    public static RPCFuture request(final Request request, ChannelFuture channelFuture) {
        RPCFuture future = new RPCFuture();
        put(request.getId(), future);
        channelFuture.channel().writeAndFlush(request).addListener(f -> {
            if (f.isSuccess()) log.info("Write data OK");
            else f.cause().printStackTrace();
        });
        return future;
    }
}
