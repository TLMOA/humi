package com.humi.jetlinks.model;

import lombok.Data;
import org.jetlinks.core.metadata.types.ObjectType;
import org.jetlinks.core.metadata.types.StringType;
import org.jetlinks.core.metadata.ConfigMetadata;
import org.jetlinks.core.metadata.DefaultConfigMetadata;
import java.util.Map;

/**
 * 忽米扩展产品模型，补充业务所需特有字段.
 */
@Data
public class HumiProductExtension {
    
    /**
     * 忽米内部产品ID
     */
    private String humiProductId;
    
    /**
     * 产品所属生产线/事业部
     */
    private String productionLine;
    
    /**
     * 设备批次号
     */
    private String batchNumber;
    
    /**
     * 产品物模型文件URL（可链接到设计图纸）
     */
    private String modelFileUrl;
    
    /**
     * 为JetLinks物模型元数据提供扩展配置.
     * @return 产品扩展的配置元数据
     */
    public static ConfigMetadata getConfigMetadata() {
        return new DefaultConfigMetadata()
                .add("humiProductId", "忽米产品ID", StringType.GLOBAL)
                .add("productionLine", "生产线", StringType.GLOBAL)
                .add("batchNumber", "批次号", StringType.GLOBAL)
                .add("modelFileUrl", "模型文件地址", new StringType().max(500));
    }
    
    /**
     * 转换为Map，用于存入产品metadata.
     * @return 产品扩展属性键值对
     */
    public Map<String, Object> toMap() {
        return Map.of(
            "humiProductId", humiProductId,
            "productionLine", productionLine,
            "batchNumber", batchNumber,
            "modelFileUrl", modelFileUrl
        );
    }
}