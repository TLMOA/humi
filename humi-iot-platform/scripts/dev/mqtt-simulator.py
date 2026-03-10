#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MQTT模拟设备脚本.
用于在开发和测试阶段模拟忽米设备上报Alink协议数据。
"""
import paho.mqtt.client as mqtt
import json
import time
import random
import logging
from datetime import datetime

# 配置日志
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class HumiSimulator:
    """忽米设备模拟器"""
    
    def __init__(self, broker_host="localhost", broker_port=1883, product_key="humi_demo", device_name="test_device_001"):
        """
        初始化模拟器
        :param broker_host: MQTT Broker地址
        :param broker_port: MQTT Broker端口
        :param product_key: 产品key
        :param device_name: 设备名称
        """
        self.broker_host = broker_host
        self.broker_port = broker_port
        self.product_key = product_key
        self.device_name = device_name
        self.device_id = f"{product_key}_{device_name}"
        
        # 初始化MQTT客户端
        self.client = mqtt.Client(client_id=self.device_id, protocol=mqtt.MQTTv311)
        self.client.on_connect = self.on_connect
        self.client.on_publish = self.on_publish
        
        # 模拟设备状态
        self.temperature = 25.0  # 温度
        self.humidity = 60.0     # 湿度
        self.power = 220.0       # 电压
        self.status = "normal"   # 设备状态
        
    def on_connect(self, client, userdata, flags, rc):
        """MQTT连接回调"""
        if rc == 0:
            logger.info(f"设备 {self.device_id} 成功连接至MQTT Broker")
        else:
            logger.error(f"连接失败，错误码: {rc}")
    
    def on_publish(self, client, userdata, mid):
        """消息发布回调"""
        logger.debug(f"消息已发布，消息ID: {mid}")
    
    def generate_alink_message(self, event_type="info_report"):
        """
        生成符合忽米Alink协议格式的消息体
        :param event_type: 事件类型，如: info_report（信息上报）、property_post（属性上报）
        :return: Alink协议JSON字符串
        """
        message = {
            "id": f"{int(time.time() * 1000)}",  # 消息ID
            "version": "1.0",
            "sys": {
                "ack": 0,
                "messageId": f"msg_{int(time.time())}"
            },
            "params": {
                "temperature": round(self.temperature + random.uniform(-0.5, 0.5), 2),
                "humidity": round(self.humidity + random.uniform(-1, 1), 1),
                "power": round(self.power + random.uniform(-0.2, 0.2), 1),
                "status": self.status,
                "timestamp": datetime.now().isoformat()
            },
            "method": f"thing.event.{event_type}"
        }
        return json.dumps(message)
    
    def connect(self, username="admin", password="public"):
        """连接MQTT Broker"""
        self.client.username_pw_set(username, password)
        self.client.connect(self.broker_host, self.broker_port, keepalive=60)
        self.client.loop_start()
    
    def start_simulation(self, interval=10, duration=3600):
        """
        开始模拟数据上报
        :param interval: 上报间隔（秒）
        :param duration: 总运行时间（秒）
        """
        logger.info(f"开始模拟设备 {self.device_id}，上报间隔: {interval}秒，总时长: {duration}秒")
        
        end_time = time.time() + duration
        message_count = 0
        
        try:
            while time.time() < end_time:
                # 随机选择上报类型
                event_type = random.choice(["info_report", "property_post"])
                payload = self.generate_alink_message(event_type)
                
                # 构造MQTT主题，符合Alink规范
                topic = f"/sys/{self.product_key}/{self.device_name}/thing/event/{event_type}"
                
                # 发布消息
                result = self.client.publish(topic, payload, qos=1)
                if result.rc == mqtt.MQTT_ERR_SUCCESS:
                    message_count += 1
                    logger.info(f"已发送第{message_count}条消息 | 主题: {topic} | 数据: {payload[:100]}...")
                
                # 等待下一个上报周期
                time.sleep(interval)
                
                # 偶尔模拟设备状态变化
                if random.random() < 0.1:  # 10%概率
                    self.status = random.choice(["normal", "warning", "error"])
                    logger.warning(f"设备状态变更为: {self.status}")
        
        except KeyboardInterrupt:
            logger.info("检测到中断信号，停止模拟...")
        finally:
            self.stop()
            logger.info(f"模拟结束，共发送 {message_count} 条消息")
    
    def stop(self):
        """停止模拟，断开连接"""
        self.client.loop_stop()
        self.client.disconnect()
        logger.info("MQTT连接已断开")

if __name__ == "__main__":
    # 使用示例
    simulator = HumiSimulator(
        broker_host="localhost",
        broker_port=1883,
        product_key="humi_pump",
        device_name="pump_001"
    )
    
    simulator.connect(username="admin", password="public")
    # 模拟运行1小时，每15秒上报一次
    simulator.start_simulation(interval=15, duration=3600)