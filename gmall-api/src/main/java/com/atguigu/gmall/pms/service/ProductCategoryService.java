package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.vo.PmsProductCategoryParam;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    void addProductCategory(PmsProductCategoryParam productCategoryParam);

    void updateProductCategory(Long id, PmsProductCategoryParam productCategoryParam);

    Map<String,Object> pageInfoByPId(Long parentId,Integer pageNum, Integer pageSize );

    void updateNavStatus(List<Long> joinId,Integer navStatus);

    void updateShowStatus(List<Long> ids,Integer showStatus);

    List<PmsProductCategoryWithChildrenItem> listWithChildrem();
}
