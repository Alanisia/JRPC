package alanisia.rpc.core.util;

import alanisia.rpc.core.proxy.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZKUtil {
    public static final int ZK_PORT = 2181;
    public static final String ZK_HOST = "127.0.0.1";
    public static final int DEFAULT_SLEEP_TIME = 1000;
    public static final String DEFAULT_ROOT_NODE = "/arpc";
    private static ZooKeeper zooKeeper;

    public static void connect(String host, int port) throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        zooKeeper = new ZooKeeper(host + ":" + port, DEFAULT_SLEEP_TIME, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                latch.countDown();
            }
        });
        latch.await();
    }

    public static void register(String host, int port) throws InterruptedException, KeeperException {
        String addr = host + ":" + port;
        if (zooKeeper == null) {
            throw new RuntimeException("ZooKeeper hasn't connected!");
        }
        if (zooKeeper.exists(DEFAULT_ROOT_NODE, true) == null) {
            zooKeeper.create(DEFAULT_ROOT_NODE, DEFAULT_ROOT_NODE.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        List<String> apiList = Proxy.getApiList();
        if (!apiList.isEmpty()) {
            for (String s : apiList) {
                String path = DEFAULT_ROOT_NODE + "/" + s;
                if (zooKeeper.exists(path, true) == null)
                    zooKeeper.create(path, addr.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                else zooKeeper.setData(path, addr.getBytes(), -1);
            }
        }
    }

    public static String discover(String api)  {
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

    public static void print() {
        try {
            ls(DEFAULT_ROOT_NODE);
        } catch (KeeperException | InterruptedException e) {
            log.error("{}", e.getMessage());
        }
    }

    private static void ls(String path) throws KeeperException, InterruptedException {
        log.info("{}", path);
        List<String> list = zooKeeper.getChildren(path, true);
        if (list == null || list.isEmpty()) return;
        for (String s : list) {
            ls(path + "/" + s);
        }
    }

    public static void close() throws InterruptedException {
        zooKeeper.close();
    }
}
