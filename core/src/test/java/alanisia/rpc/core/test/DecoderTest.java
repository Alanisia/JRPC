
package alanisia.rpc.core.test;

import alanisia.rpc.core.handler.RPCDecoder;
import alanisia.rpc.core.model.Response;
import alanisia.rpc.core.serialize.Serializer;
import alanisia.rpc.core.util.constant.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class DecoderTest {
    @Test
    public void testByteToMessage() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) {
                channel.pipeline().addLast(new RPCDecoder(Response.class));
            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(i);
        Response response = new Response(System.currentTimeMillis(), "xxx", Constant.SUCCESS, true);
        byte[] bytes = Serializer.serialize(response);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        embeddedChannel.writeInbound(buf);
        Thread.sleep(2000);
    }
}
