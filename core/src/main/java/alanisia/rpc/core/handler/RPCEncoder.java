package alanisia.rpc.core.handler;

import alanisia.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class RPCEncoder extends MessageToByteEncoder<Object> {
    private final Class<?> target;

    public RPCEncoder(Class<?> target) { this.target = target; }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object t, ByteBuf byteBuf) {
        log.info("Encode: {}", target.getName());
        if (target.isInstance(t)) {
            try {
                byte[] bytes = Serializer.serialize(t);
                byteBuf.writeInt(bytes.length);
                byteBuf.writeBytes(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
