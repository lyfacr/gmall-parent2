package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductAttributeCategory;
import com.atguigu.gmall.pms.vo.PmsProductAttributeCategoryItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductAttributeCategoryService extends IService<ProductAttributeCategory> {

    boolean addPmsProductAttribute(String name);

    boolean updatePmsProductAttribute(Long id, String name);

    Map<String,Object> pageInfo(Integer pageNum, Integer pageSize);

    PmsProductAttributeCategoryItem getListWithAttr();
}
