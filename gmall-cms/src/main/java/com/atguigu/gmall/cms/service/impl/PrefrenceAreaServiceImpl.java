package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.PrefrenceArea;
import com.atguigu.gmall.cms.mapper.PrefrenceAreaMapper;
import com.atguigu.gmall.cms.service.PrefrenceAreaService;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * <p>
 * 优选专区 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class PrefrenceAreaServiceImpl extends ServiceImpl<PrefrenceAreaMapper, PrefrenceArea> implements PrefrenceAreaService {

    @Reference
    ProductService productService;

    @Override
    public List<Product> listAllPrenceProduct() {
        PrefrenceAreaMapper baseMapper = getBaseMapper();
        return null;
    }
}
