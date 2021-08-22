package alanisia.rpc.server;

import alanisia.rpc.core.Server;
import alanisia.rpc.core.annotation.RPCScan;
import alanisia.rpc.core.proxy.Proxy;
import alanisia.rpc.core.util.ZKUtil;
import alanisia.rpc.core.util.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

@Slf4j
@RPCScan(value = {"alanisia.rpc.server.impl"})
public class RpcServer {
	public static void main(String[] args) {
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
