package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.mapper.ProductMapper;
import com.atguigu.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Map<String, Object> pageInfo(Integer pageSize, Integer pageNum) {
        ProductMapper baseMapper = getBaseMapper();
        Page<Product> page = new Page<>();
        IPage<Product> selectPage = baseMapper.selectPage(page, null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",selectPage.getSize());
        map.put("totalPage",selectPage.getTotal());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());

        return map;
    }
}
