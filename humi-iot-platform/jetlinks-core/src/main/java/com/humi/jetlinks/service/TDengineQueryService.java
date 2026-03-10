// src/main/java/com/humi/jetlinks/service/TDengineQueryService.java
package com.humi.jetlinks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TDengineQueryService {

@Autowired(required = false) // 因为TDengine可能只在生产环境配置
private JdbcTemplate tdengineJdbcTemplate;

/**
     * 查询设备历史数据
     * @param deviceId 设备ID
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param metrics 需要查询的指标列表，如 temperature, humidity
     * @return 数据列表
*/
public List<Map<String, Object>> queryHistory(String deviceId, long startTime, long endTime, List<String> metrics) {
if (tdengineJdbcTemplate == null) {
throw new IllegalStateException("TDengine 数据源未配置");
}
// 构建TDengine SQL，例如查询名为 `device_${deviceId}` 的超表
String metricFields = String.join(", ", metrics);
String sql = String.format(
"SELECT ts, %s FROM device_data WHERE device_id = ? AND ts >= ? AND ts <= ? ORDER BY ts",
metricFields
);
return tdengineJdbcTemplate.queryForList(sql, deviceId, new Timestamp(startTime), new Timestamp(endTime));
}
}