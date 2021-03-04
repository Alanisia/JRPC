package alanisia.rpc;

import alanisia.rpc.server.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcApplication.class, args);
		new Server(4000).server();
	}

}
