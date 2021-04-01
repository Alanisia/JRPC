package alanisia.rpc.core.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String host;
    private int port;
}
