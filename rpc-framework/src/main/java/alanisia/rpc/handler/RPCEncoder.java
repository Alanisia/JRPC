package alanisia.rpc.handler;

import alanisia.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RPCEncoder<T> extends MessageToByteEncoder<T> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, T t, ByteBuf byteBuf) throws Exception {
        byte[] bytes = Serializer.serialize(t);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
