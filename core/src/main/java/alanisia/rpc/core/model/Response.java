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
public class Response implements Serializable {
    private static final long serialVersionUID = 4860784028791808887L;

    private long id;
    private String methodName;
    private int status;
    private Object result;
}
