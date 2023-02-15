package com.yang.netty.codec.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;


/**
 * @Description:
 * @Author zhangyang03
 * @Date 2023/2/15 16:38
 */
public class MarshallingCodeCFactory {

    /**
     * @return 创建Jboss Marshalling解码器MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        return new MarshallingDecoder(provider, 1024);
    }

    /**
     * @return 创建Jboss Marshalling编码器MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        return new MarshallingEncoder(provider);
    }
}
