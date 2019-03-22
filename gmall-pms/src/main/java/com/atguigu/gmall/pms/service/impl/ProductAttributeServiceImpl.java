package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.mapper.ProductAttributeMapper;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.pms.vo.PmsProductAttributeParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Override
    public Map<String, Object> pageInfo(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        ProductAttributeMapper baseMapper = getBaseMapper();
        //根据分类查询属性列表或参数列表 0表示属性，1表示参数
        Page<ProductAttribute> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<ProductAttribute>().eq("type", type)
                .eq("product_attribute_category_id", cid);
        IPage<ProductAttribute> selectPage = baseMapper.selectPage(page, queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",selectPage.getSize());
        map.put("totalPage",selectPage.getTotal());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());
        return map;
    }

    @Override
    public void addPmsProductAttribute(PmsProductAttributeParam productAttributeParam) {
        ProductAttributeMapper baseMapper = getBaseMapper();
        ProductAttribute productAttribute = new ProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        baseMapper.insert(productAttribute);
    }

    @Override
    public void updatePmsProductAttribute(Long id, PmsProductAttributeParam productAttributeParam) {
        ProductAttributeMapper baseMapper = getBaseMapper();
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setId(id);
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        baseMapper.updateById(productAttribute);
    }

    @Override
    public void getAttrInfo(Long productCategoryId) {
        //根据商品分类的id获取商品属性及属性分类
        //根据分类查询属性列表或参数列表
        ProductAttributeMapper baseMapper = getBaseMapper();

        QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("productAttributeCategoryId",productCategoryId);
        List<ProductAttribute> productAttributes = baseMapper.selectList(queryWrapper);

    }
}
