package alanisia.rpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcServer {
	public static void main(String[] args) {
		SpringApplication.run(RpcServer.class, args);
		new Server(4000).server();
	}
}
