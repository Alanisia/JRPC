package alanisia.rpc.registry;

import org.apache.zookeeper.ZooKeeper;

public class ZKConnector {
    public static void connectZKCluster(String host, int port, int sessionTimeout) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(host + port, sessionTimeout, watchedEvent -> {

        });
    }

}
