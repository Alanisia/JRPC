package alanisia.rpc.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration
public class ZooKeeperConfiguration {
    public static final int ZK_PORT = 2181;
    public static final String ZK_HOST = "127.0.0.1";
    public static final int DEFAULT_SLEEP_TIME = 1000;

    @Bean("zookeeper")
    public ZooKeeper connect() throws InterruptedException, IOException {
        log.info("Register zookeeper");
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(ZK_HOST + ":" + ZK_PORT, DEFAULT_SLEEP_TIME, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                latch.countDown();
            }
        });
        latch.await();
        return zooKeeper;
    }
}
