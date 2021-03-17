package alanisia.rpc.core.handler;

import alanisia.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RPCDecoder extends ByteToMessageDecoder {
    private final Class<?> target;

    public RPCDecoder(Class<?> target) { this.target = target; }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        log.info("Decode, {}", target.getName());
        if (byteBuf.readableBytes() >= 4) {
            byteBuf.markReaderIndex();
            int length = byteBuf.readInt();
            if (length > byteBuf.readableBytes()) byteBuf.resetReaderIndex();
            else {
                byte[] bytes = new byte[length];
                byteBuf.readBytes(bytes);
                Object object = Serializer.deserialize(bytes, target);
                list.add(object);
            }
        }
    }
}
