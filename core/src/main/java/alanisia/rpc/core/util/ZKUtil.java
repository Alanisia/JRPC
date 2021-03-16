package alanisia.rpc.core.util;

import alanisia.rpc.core.annotation.RPC;
import alanisia.rpc.core.annotation.RPCScan;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ZKUtil {
    public static final int ZK_PORT = 2181;
    public static final String ZK_HOST = "127.0.0.1";
    public static final int DEFAULT_SLEEP_TIME = 1000;
    public static final String DEFAULT_ROOT_NODE = "/a-rpc";
    private static ZooKeeper zooKeeper;

    public static void connectZKCluster(String host, int port)
            throws InterruptedException, KeeperException, IOException {
        zooKeeper = new ZooKeeper(host + ":" + port, DEFAULT_SLEEP_TIME, null);
    }

    public static void register(Class<?> clazz) throws InterruptedException, KeeperException {
        if (zooKeeper == null) {
            throw new RuntimeException("ZooKeeper hasn't connected!");
        }
        List<String> packages = AnnotationUtil.getValues(clazz, RPCScan.class, "value");
        Set<Set<Class<?>>> classSets = new HashSet<>();
        if (packages != null) {
            for (String paquete : packages) {
                Set<Class<?>> classSet = AnnotationUtil.scan(paquete, RPC.class);
                classSets.add(classSet);
            }
        }
        if (zooKeeper.exists(DEFAULT_ROOT_NODE, true) == null) {
            zooKeeper.create(DEFAULT_ROOT_NODE, DEFAULT_ROOT_NODE.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

    }

    public static String getDataFromServer(String api)  {
        try {
            String path = DEFAULT_ROOT_NODE + "/" + api;
            Stat exists = zooKeeper.exists(path, true);
            if (exists != null) {
                byte[] data = zooKeeper.getData(path, true, new Stat());
                return new String(data);
            }
        } catch (KeeperException | InterruptedException e) {
            log.error("Failed to get node, cause: {}", e.getMessage());
            System.exit(0);
        }
        return null;
    }
}
