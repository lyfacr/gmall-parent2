package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.entity.ProductCategoryAttributeRelation;
import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.pms.service.ProductCategoryAttributeRelationService;
import com.atguigu.gmall.pms.service.ProductCategoryService;
import com.atguigu.gmall.pms.vo.PmsProductCategoryParam;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Slf4j
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductCategoryAttributeRelationService productCategoryAttributeRelationService;

    @Override
    public void addProductCategory(PmsProductCategoryParam productCategoryParam) {
        ProductCategoryMapper baseMapper = getBaseMapper();
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryParam,productCategory);
        baseMapper.insert(productCategory);
        List<Long> productAttributeIdList = productCategoryParam.getProductAttributeIdList();
        for (int i = 0; i < productAttributeIdList.size(); i++) {
            Long productAttributeId = productAttributeIdList.get(i);
            ProductCategoryAttributeRelation relation = new ProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttributeId);
            relation.setProductCategoryId(productCategory.getId());
            productCategoryAttributeRelationService.save(relation);
        }

    }

    @Override
    public void updateProductCategory(Long id, PmsProductCategoryParam productCategoryParam) {
        ProductCategoryMapper baseMapper = getBaseMapper();

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(productCategoryParam,productCategory);

        baseMapper.updateById(productCategory);

        List<Long> productAttributeIdList = productCategoryParam.getProductAttributeIdList();
        for (int i = 0; i < productAttributeIdList.size(); i++) {
            Long productAttributeId = productAttributeIdList.get(i);
            ProductCategoryAttributeRelation relation = new ProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttributeId);
            QueryWrapper<ProductCategoryAttributeRelation> queryWrapper = new QueryWrapper<ProductCategoryAttributeRelation>().eq("product_category_id", id);

            productCategoryAttributeRelationService.update(relation,queryWrapper);
        }
    }

    @Override
    public Map<String, Object> pageInfoByPId(Long parentId, Integer pageNum, Integer pageSize) {
        ProductCategoryMapper baseMapper = getBaseMapper();

        Page<ProductCategory> page = new Page<>(pageNum,pageSize);
        QueryWrapper<ProductCategory> queryWrapper = null;
        if(!StringUtils.isEmpty(parentId)){
            queryWrapper = new QueryWrapper<ProductCategory>().eq("parent_id", parentId);
        }
        IPage<ProductCategory> selectPage = baseMapper.selectPage(page, queryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",selectPage.getSize());
        map.put("totalPage",selectPage.getTotal());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());

        return map;
    }

    @Override
    public void updateNavStatus(List<Long> joinId, Integer navStatus) {
        ProductCategoryMapper baseMapper = getBaseMapper();
        List<ProductCategory> productCategories = baseMapper.selectBatchIds(joinId);
        for (int i = 0; i < productCategories.size(); i++) {
            ProductCategory productCategory = productCategories.get(i);
            productCategory.setNavStatus(navStatus);
            baseMapper.updateById(productCategory);
        }
    }

    @Override
    public void updateShowStatus(List<Long> ids,Integer showStatus) {
        ProductCategoryMapper baseMapper = getBaseMapper();
        List<ProductCategory> productCategories = baseMapper.selectBatchIds(ids);
        for (int i = 0; i < productCategories.size(); i++) {
            ProductCategory productCategory = productCategories.get(i);
            productCategory.setShowStatus(showStatus);
            baseMapper.updateById(productCategory);
        }
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildrem() {

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cache = ops.get(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY);
        if(!StringUtils.isEmpty(cache)){
            log.debug("缓存命中");
            List<PmsProductCategoryWithChildrenItem> items = JSON.parseArray(cache, PmsProductCategoryWithChildrenItem.class);
            return  items;
        }
        log.debug("缓存未命中");
        //查询一级分类
        ProductCategoryMapper baseMapper = getBaseMapper();
        List<PmsProductCategoryWithChildrenItem> items = baseMapper.listWithChildren(0);
        String jsonString = JSON.toJSONString(items);
        ops.set(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY,jsonString,3, TimeUnit.DAYS);

        return items;

    }
//    private  List<ProductCategory> foreachItem(List<ProductCategory> items,List<ProductCategory> categories) {
////        List<ProductCategory> item = items.getChildren();
//        for (int i = 0; i < items.size(); i++) {
//
//            ProductCategory productCategory = items.get(i);
//            categories.add(productCategory);
//            QueryWrapper<ProductCategory> chirdWrapper = new QueryWrapper<ProductCategory>()
//                    .eq("parent_id", productCategory.getId());
//            List<ProductCategory> subList = baseMapper.selectList(chirdWrapper);
//            if (!StringUtils.isEmpty(subList)) {
//                foreachItem(subList,items);
//            }
//        }
//        return categories;
//    }

}
