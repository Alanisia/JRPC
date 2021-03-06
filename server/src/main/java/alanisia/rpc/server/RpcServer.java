package alanisia.rpc.server;

import alanisia.rpc.annotation.RPCScan;
import alanisia.rpc.util.Server;
import alanisia.rpc.util.ZKUtil;
import alanisia.rpc.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RPCScan(basePackage = {"alanisia.rpc.server.impl"})
public class RpcServer {
	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);
		try {
			ZKUtil.connectZKCluster(ZKUtil.ZK_HOST, ZKUtil.ZK_PORT);
			new Server(Constant.SERVER_PORT).server();
		} catch (Exception e) {
			log.error("{}", e.getMessage());
		}
	}
}
