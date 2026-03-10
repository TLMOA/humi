package com.humi.jetlinks.service;

import com.humi.jetlinks.model.HumiProductExtension;
import org.jetlinks.core.product.ProductInfo;
import org.jetlinks.core.product.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Map;

/**
 * 针对忽米扩展产品模型的操作服务.
 */
@Service
public class HumiProductService {
    
    @Autowired
    private ProductManager productManager;
    
    /**
     * 为现有产品添加忽米扩展信息.
     * @param productId JetLinks产品ID
     * @param extension 扩展信息
     * @return 操作结果
     */
    public Mono<Boolean> saveExtension(String productId, HumiProductExtension extension) {
        return productManager.getProduct(productId)
                .flatMap(product -> {
                    // 获取原有metadata
                    ProductInfo info = product.getInfo();
                    Map<String, Object> metadata = info.getMetadata();
                    // 合并扩展信息
                    metadata.putAll(extension.toMap());
                    // 更新产品信息 (需根据JetLinks实际API调整)
                    return productManager.updateProduct(product);
                })
                .thenReturn(true)
                .onErrorReturn(false);
    }
    
    /**
     * 根据忽米产品ID查找JetLinks产品.
     * @param humiProductId 忽米内部产品ID
     * @return 匹配的产品信息
     */
    public Mono<ProductInfo> findByHumiProductId(String humiProductId) {
        // 示例：可能需要遍历所有产品进行筛选
        // 实际应用中，应考虑缓存或建立反向索引以提高性能
        return productManager.getAllProducts()
                .filter(product -> {
                    Map<String, Object> metadata = product.getInfo().getMetadata();
                    return humiProductId.equals(metadata.get("humiProductId"));
                })
                .next()
                .map(ProductInfo.class::cast);
    }
}