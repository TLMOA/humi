package com.humi.jetlinks.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 忽米扩展设备模型，补充业务所需特有字段.
 */
@Data
public class HumiDeviceExtension {
    
    /**
     * 设备唯一SN码
     */
    private String serialNumber;
    
    /**
     * 设备生产日期
     */
    private LocalDateTime manufactureDate;
    
    /**
     * 首次上线时间
     */
    private LocalDateTime firstOnlineTime;
    
    /**
     * 质保截止日期
     */
    private LocalDateTime warrantyUntil;
    
    /**
     * 设备物理位置描述（如：XX工厂-产线A-工位3）
     */
    private String location;
    
    /**
     * 设备健康评分 (0-100)
     */
    private Integer healthScore;
}