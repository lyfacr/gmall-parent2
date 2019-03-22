package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.vo.PmsProductAttributeParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    Map<String,Object> pageInfo(Long cid, Integer type, Integer pageNum, Integer pageSize);

    void addPmsProductAttribute(PmsProductAttributeParam productAttributeParam);

    void updatePmsProductAttribute(Long id,PmsProductAttributeParam productAttributeParam);

    void getAttrInfo(Long productCategoryId);
}
