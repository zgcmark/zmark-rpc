package com.zmark.remoting.provider.netty.handler;

import com.zmark.core.helper.JsonHelper;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocol;
import com.zmark.remoting.provider.netty.protocol.ZmarkProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zhengguangchen
 */

public class ZmarkRpcEncoder extends MessageToByteEncoder<ZmarkProtocol<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ZmarkProtocol zmarkProtocol, ByteBuf byteBuf) throws Exception {
        ZmarkProtocolHeader header = zmarkProtocol.getProtocolHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeInt(header.getMsgType());
        byteBuf.writeLong(header.getRequestId());
        byteBuf.writeInt(header.getDataSize());

        //写数据
        String s = JsonHelper.toJsonString(zmarkProtocol.getBody());
        byte[] dataBytes = s.getBytes();
        byteBuf.writeBytes(dataBytes);
    }
}
