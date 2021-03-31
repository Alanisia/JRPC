package alanisia.rpc.server;

import alanisia.rpc.core.annotation.RPCScan;
import alanisia.rpc.core.proxy.Proxy;
import alanisia.rpc.core.Server;
import alanisia.rpc.core.util.ZKUtil;
import alanisia.rpc.core.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetAddress;

@Slf4j
@SpringBootApplication
@ComponentScan({"alanisia.rpc.core"})
@RPCScan(value = {"alanisia.rpc.server.impl"})
public class RpcServer {
	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);
		try {
			Proxy.initProxyMap(RpcServer.class);
			Proxy.print();
			ZKUtil.connect(ZKUtil.ZK_HOST, ZKUtil.ZK_PORT);
			ZKUtil.register(InetAddress.getLocalHost().getHostAddress(), Constant.SERVER_PORT);
			new Server(Constant.SERVER_PORT).server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
