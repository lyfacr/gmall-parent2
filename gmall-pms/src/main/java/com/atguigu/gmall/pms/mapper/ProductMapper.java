package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductMapper extends BaseMapper<Product> {

    void updatebatchPublishStatus(@Param("ids")List<Long> ids, Integer publishStatus);

    void updateBatchRecommendStatus(@Param("ids")List<Long> ids, Integer recommendStatus);

    void updateBatchNewStatus(@Param("ids")List<Long> ids, Integer newStatus);

    void updateBatchDeleteStatus(@Param("ids")List<Long> ids, Integer deleteStatus);

    List<EsProductAttributeValue> getProductSaleAttr(Long productId);

    List<EsProductAttributeValue> getProductBaseAttr(Long productId);
}

