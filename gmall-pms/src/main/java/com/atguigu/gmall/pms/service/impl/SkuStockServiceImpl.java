package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.mapper.SkuStockMapper;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

    @Autowired
    SkuStockMapper skuStockMapper;

    @Override
    public void getListByPIdAndKey(Long pid, String keyword) {
        SkuStockMapper baseMapper = getBaseMapper();
        QueryWrapper<SkuStock> queryWrapper = new QueryWrapper<SkuStock>().eq("product_id", pid)
                .like("sku_code", keyword);
        List<SkuStock> selectList = baseMapper.selectList(queryWrapper);

    }

    @Override
    public void updateStock(Long pid, List<SkuStock> skuStockList) {
        SkuStockMapper baseMapper = getBaseMapper();
        QueryWrapper<SkuStock> queryWrapper = new QueryWrapper<SkuStock>().eq("product_id", pid);
        List<SkuStock> skuStocks = baseMapper.selectList(queryWrapper);

        skuStockList.forEach(sku->{
            baseMapper.updateById(sku);
        });
//        baseMapper.updateBatchStock(ids,skuStockList);
    }

    @Override
    public List<SkuStock> getAllSkuInfoByProductId(Long productId) {

        return skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id",productId));

    }
}
