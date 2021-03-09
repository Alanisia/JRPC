package alanisia.rpc.server;

import alanisia.rpc.annotation.RPCScan;
import alanisia.rpc.proxy.Proxy;
import alanisia.rpc.util.Server;
import alanisia.rpc.util.ZKUtil;
import alanisia.rpc.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan({"alanisia.rpc"})
@RPCScan(value = {"alanisia.rpc.server.impl"})
public class RpcServer {
	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);
		try {
			// ZKUtil.connectZKCluster(ZKUtil.ZK_HOST, ZKUtil.ZK_PORT);
			Proxy proxy = new Proxy();
			proxy.initProxyMap(RpcServer.class);

			new Server(Constant.SERVER_PORT).server();
		} catch (Exception e) {
			// log.error("{}", e.getStackTrace());
			e.printStackTrace();
		}
	}
}
