package alanisia.rpc.core.test;

import alanisia.rpc.core.handler.RPCEncoder;
import alanisia.rpc.core.model.Request;
import alanisia.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;

public class EncoderTest {
    @Test
    public void testMessageToByte() throws InterruptedException {
        Request request = new Request();
        request.setId(System.currentTimeMillis());
        request.setMethodName("xxx");
        request.setClazz(Object.class);
        request.setParams(new Integer[]{1, 2, 3});
        request.setParamTypes(new Class[]{Integer.class, Integer.class, Integer.class});
        ChannelInitializer<EmbeddedChannel> ci = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) {
                embeddedChannel.pipeline().addLast(new RPCEncoder(Request.class));
            }
        };
        // ByteBuf buf = Unpooled.buffer();
        // buf.writeBytes(new ObjectInputStream());

        EmbeddedChannel channel = new EmbeddedChannel(ci);
        channel.writeOutbound(request);
        Thread.sleep(2000);
    }
}
