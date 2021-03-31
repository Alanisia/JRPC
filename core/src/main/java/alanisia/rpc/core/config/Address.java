package alanisia.rpc.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String host;
    private int port;
}
