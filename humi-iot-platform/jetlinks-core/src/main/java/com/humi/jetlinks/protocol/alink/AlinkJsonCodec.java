// src/main/java/com/humi/jetlinks/protocol/alink/AlinkJsonCodec.java
package com.humi.jetlinks.protocol.alink;

import org.jetlinks.core.message.codec.*;
import io.netty.buffer.Unpooled;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

public class AlinkJsonCodec implements DeviceMessageCodec {

@Override
public Transport getSupportTransport() {
return DefaultTransport.MQTT; // 假设使用MQTT传输
}

@Nonnull
@Override
public EncodedMessage encode(@Nonnull MessageEncodeContext context) {
// 编码（下行）逻辑，根据项目需求实现
throw new UnsupportedOperationException("encode not supported yet");
}

@Nonnull
@Override
public DeviceMessage decode(@Nonnull MessageDecodeContext context) {
try {
String payload = context.getMessage()
.getPayload()
.toString(StandardCharsets.UTF_8);
JSONObject json = JSON.parseObject(payload);

// 1. 提取忽米Alink协议中的设备标识符，如 productKey, deviceName
String productKey = json.getString("productKey");
String deviceName = json.getString("deviceName");

// 2. 根据协议格式，构造JetLinks标准的设备消息
// 例如，处理属性上报
if ("thing.event.property.post".equals(json.getString("method"))) {
return createPropertiesReportMessage(productKey, deviceName, json);
}
// 处理其他类型消息...

return UnknownDeviceMessage.of(productKey, deviceName, json);
} catch (Exception e) {
throw new CodecException("解码Alink消息失败", e);
}
}

private DeviceMessage createPropertiesReportMessage(String productKey, String deviceName, JSONObject json) {
// 解析Alink中的params，转换为JetLinks的属性映射
// 这是您需要根据实际协议详细实现的部分
ReportPropertyMessage message = new ReportPropertyMessage();
message.setDeviceId(DeviceId.of(productKey, deviceName)); // 假设用此方式组合ID
message.setTimestamp(System.currentTimeMillis());
// message.setProperties(...);
return message;
}
}