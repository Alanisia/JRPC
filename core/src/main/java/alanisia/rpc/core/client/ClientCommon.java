package alanisia.rpc.core.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientCommon {
    private static Map<Address, Client> clientMap = new ConcurrentHashMap<>();

    public static Client get(Address address) {
        return clientMap.get(address);
    }

    public static void put(String host, int port, Client client) {
        clientMap.put(new Address(host, port), client);
    }

    public static boolean contains(Address address) {
        return clientMap.containsKey(address);
    }

    public static void shutdownAll() {
        try {
            Thread.sleep(1000);
            for (Map.Entry<Address, Client> entry : clientMap.entrySet()) {
                entry.getValue().shutdown();
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }
}
