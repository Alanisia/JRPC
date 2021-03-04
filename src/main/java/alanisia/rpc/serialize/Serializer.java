package alanisia.rpc.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serializer<T> {
    private static final Kryo kryo = new Kryo();

    public static synchronized byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, object);
        return output.toBytes();
    }

    public static synchronized <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(inputStream);
        Object o = kryo.readObject(input, clazz);
        return clazz.cast(o);
    }
}
