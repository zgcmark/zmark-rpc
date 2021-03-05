package com.zmark.remoting.provider.netty.codec;

import com.zmark.core.helper.JsonHelper;
import com.zmark.remoting.provider.netty.enums.MsgTypeEnum;
import com.zmark.remoting.provider.netty.protocol.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zhengguangchen
 */
//  魔数2byte  消息类型4byte  消息ID 8byte  数据长度4byte 数据内容
public class ZmarkRpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < ZmarkProtocolType.HEADER_TOTAL_LEN) {
            return;
        }

        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != ZmarkProtocolType.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        int msgType = byteBuf.readInt();
        MsgTypeEnum msgTypeEnum = MsgTypeEnum.valueOfType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        long requestId = byteBuf.readLong();

        int dataSize = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataSize) {
            //半包情况 不是完整的流，不处理
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] dataByte = new byte[dataSize];
        byteBuf.readBytes(dataByte);

        //封装对象
        ZmarkProtocolHeader header = new ZmarkProtocolHeader();
        header.setMagic(magic);
        header.setDataSize(dataSize);
        header.setMsgType(msgType);
        header.setRequestId(requestId);

        switch (msgTypeEnum) {
            case request:
                ZmarkRpcRequest request = JsonHelper.json2Object(dataByte, ZmarkRpcRequest.class);
                ZmarkProtocol<ZmarkRpcRequest> zmarkProtocol = new ZmarkProtocol();
                zmarkProtocol.setProtocolHeader(header);
                zmarkProtocol.setBody(request);
                out.add(zmarkProtocol);
                break;
            case response:
                ZmarkRpcResponse response = JsonHelper.json2Object(dataByte, ZmarkRpcResponse.class);
                ZmarkProtocol<ZmarkRpcResponse> responseZmarkProtocol = new ZmarkProtocol();
                responseZmarkProtocol.setProtocolHeader(header);
                responseZmarkProtocol.setBody(response);
                out.add(responseZmarkProtocol);
                break;
        }

    }
}
