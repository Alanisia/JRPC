package alanisia.rpc.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Request implements Serializable {
    private static final long serialVersionUID = 3105683842143416822L;

    private long id;
    private Class<?> clazz;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramTypes;
    private String version;
}
