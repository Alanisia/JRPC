package alanisia.rpc.core.client;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientCommon {
    private static Map<Address, ChannelFuture> channelFutureMap = new ConcurrentHashMap<>();

    public static ChannelFuture get(Address address) {
        return channelFutureMap.get(address);
    }

    public static void put(String host, int port, ChannelFuture channelFuture) {
        channelFutureMap.put(new Address(host, port), channelFuture);
    }

    public static boolean contains(Address address) {
        return channelFutureMap.containsKey(address);
    }
}
