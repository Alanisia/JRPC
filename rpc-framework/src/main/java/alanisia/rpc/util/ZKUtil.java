package alanisia.rpc.util;

import alanisia.rpc.annotation.RPC;
import alanisia.rpc.util.constant.ZKConstant;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Set;

public class ZKUtil {
    private static ZooKeeper zooKeeper;

    public static void connectZKCluster(String host, int port, int sessionTimeout)
            throws InterruptedException, KeeperException, IOException {
        zooKeeper = new ZooKeeper(host + ":" + port, sessionTimeout, null);
    }

    public static void register() throws InterruptedException, KeeperException {
        if (zooKeeper == null) {
            throw new RuntimeException("ZooKeeper hasn't connected!");
        }
        // TODO
        // List<String> packages = AnnotationUtil.getValues();
        Set<Class<?>> classSet = AnnotationUtil.scan("", RPC.class);
        if (zooKeeper.exists(ZKConstant.DEFAULT_ROOT_NODE, true) == null) {
            zooKeeper.create(ZKConstant.DEFAULT_ROOT_NODE, ZKConstant.DEFAULT_ROOT_NODE.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

    }

}
