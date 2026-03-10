package com.humi.jetlinks.protocol.alink;

import org.jetlinks.core.ProtocolSupport;
import org.jetlinks.core.defaults.CompositeProtocolSupport;
import org.jetlinks.core.message.codec.DeviceMessageCodec;
import org.jetlinks.supports.official.JetLinksOfficialProtocol;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Collections;

@Component
public class AlinkProtocolProvider implements ProtocolSupport {

private final CompositeProtocolSupport support = new CompositeProtocolSupport();

public AlinkProtocolProvider() {
support.setId("humi-alink"); // 协议唯一ID
support.setName("忽米Alink协议");
support.setDescription("用于接入忽米Alink格式的物联网设备");
// 继承官方协议的部分特性
support.addSupport(JetLinksOfficialProtocol.getInstance());
// 添加自定义编解码器
support.addCodecSupport(new AlinkJsonCodec());
}

@Override
public String getId() {
return support.getId();
}

@Override
public Mono<? extends DeviceMessageCodec> getMessageCodec(DeviceMessageCodecConfiguration configuration) {
// 返回您的解码器实例
return Mono.just(new AlinkJsonCodec());
}
// ... 实现其他必要方法，通常直接委托给 `support` 对象
}