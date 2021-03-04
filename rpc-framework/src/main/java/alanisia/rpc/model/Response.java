package alanisia.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response implements Serializable {
    private static final long serialVersionUID = 4860784028791808887L;

    private long id;
    private String methodName;
    private int status;
    private Object result;
}
