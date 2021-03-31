package alanisia.rpc.core.config;

import alanisia.rpc.core.Client;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConfiguration {
    private static Map<Address, Client> clientMap = new ConcurrentHashMap<>();
    // private static Map<Address, ChannelFuture> channelFutureMap = new ConcurrentHashMap<>();

//    public static void add(String host, int port, ChannelFuture channelFuture) {
//        put(new Address(host, port), channelFuture);
//    }
//
//    public static void put(Address address, ChannelFuture channelFuture) {
//        channelFutureMap.put(address, channelFuture);
//    }
//
//    public static ChannelFuture get(Address address) {
//        return channelFutureMap.get(address);
//    }

    public static Client get(Address address) {
        return clientMap.get(address);
    }

    public static void put(String host, int port, Client client) {
        clientMap.put(new Address(host, port), client);
    }

    public static boolean contains(Address address) {
        return clientMap.containsKey(address);
    }
}
