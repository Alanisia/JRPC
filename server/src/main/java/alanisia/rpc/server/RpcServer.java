package alanisia.rpc.server;

import alanisia.rpc.util.Server;
import alanisia.rpc.util.constant.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcServer {
	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);
		new Server(Constant.SERVER_PORT).server();
	}
}
