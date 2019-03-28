package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.ProductCategoryAttributeRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品的分类和属性的关系表，用于设置分类筛选条件 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductCategoryAttributeRelationMapper extends BaseMapper<ProductCategoryAttributeRelation> {

    void addBatchCategoryAttribute(@Param("ids") List<Long> ids, Long cid);

    void updateBatchCategoryAttribute(@Param("ids")List<Long> ids, Long cid);
}
