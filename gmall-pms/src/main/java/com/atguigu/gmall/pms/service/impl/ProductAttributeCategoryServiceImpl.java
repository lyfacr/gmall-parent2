package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.entity.ProductAttributeCategory;
import com.atguigu.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.atguigu.gmall.pms.mapper.ProductAttributeMapper;
import com.atguigu.gmall.pms.service.ProductAttributeCategoryService;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.pms.vo.PmsProductAttributeCategoryItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

    @Autowired
    ProductAttributeMapper productAttributeMapper;
    @Override
    public boolean addPmsProductAttribute(String name) {
        ProductAttributeCategoryMapper baseMapper = getBaseMapper();
        ProductAttributeCategory productAttributeCategory = new ProductAttributeCategory();
        productAttributeCategory.setName(name);
        Integer result = baseMapper.insert(productAttributeCategory);
        return null != result && result>0;
    }

    @Override
    public boolean updatePmsProductAttribute(Long id, String name) {
        ProductAttributeCategoryMapper baseMapper = getBaseMapper();
        ProductAttributeCategory productAttributeCategory = new ProductAttributeCategory();
        productAttributeCategory.setName(name);
        Integer result = baseMapper.updateById(productAttributeCategory);
        return null != result && result>0;
    }

    @Override
    public Map<String, Object> pageInfo(Integer pageNum, Integer pageSize) {
        ProductAttributeCategoryMapper baseMapper = getBaseMapper();
        Page<ProductAttributeCategory> page = new Page<>(pageNum, pageSize);
        IPage<ProductAttributeCategory> selectPage = baseMapper.selectPage(page, null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",selectPage.getSize());
        map.put("totalPage",selectPage.getTotal());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());

        return map;
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        ProductAttributeCategoryMapper baseMapper = getBaseMapper();
        List<ProductAttributeCategory> categories = baseMapper.selectList(null);
        ArrayList<PmsProductAttributeCategoryItem> items = new ArrayList<>();

        categories.forEach(categorie->{
            PmsProductAttributeCategoryItem attributeCategoryItem = new PmsProductAttributeCategoryItem();
            BeanUtils.copyProperties(categorie,attributeCategoryItem);
            QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<ProductAttribute>()
                    .eq("product_attribute_category_id", categorie.getId());
            List<ProductAttribute> productAttributes = productAttributeMapper.selectList(queryWrapper);

            attributeCategoryItem.setProductAttributeList(productAttributes);
            items.add(attributeCategoryItem);
        });

        return items;
    }
}
