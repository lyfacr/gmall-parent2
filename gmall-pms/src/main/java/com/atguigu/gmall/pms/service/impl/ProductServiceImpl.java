package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.mapper.ProductMapper;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.List;
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
    public Map<String, Object> pageInfo(Integer pageSize, Integer pageNum,PmsProductQueryParam productQueryParam) {
        ProductMapper baseMapper = getBaseMapper();
        QueryWrapper<Product> queryWrapper = queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(productQueryParam.getPublishStatus())){
            queryWrapper.eq("publish_status",productQueryParam.getPublishStatus());
        }
        if(!StringUtils.isEmpty(productQueryParam.getVerifyStatus())){
            queryWrapper.eq("verify_status",productQueryParam.getVerifyStatus());
        }
        if(!StringUtils.isEmpty(productQueryParam.getKeyword())){
            queryWrapper.like("name",productQueryParam.getVerifyStatus())
            .like("sub_title",productQueryParam.getKeyword())
            .like("description",productQueryParam.getKeyword());
        }
        if(!StringUtils.isEmpty(productQueryParam.getProductSn())){
            queryWrapper.eq("product_sn",productQueryParam.getProductSn());
        }
        if(!StringUtils.isEmpty(productQueryParam.getProductCategoryId())){
            queryWrapper.eq("product_category_id",productQueryParam.getProductCategoryId());
        }
        if(!StringUtils.isEmpty(productQueryParam.getBrandId())){
            queryWrapper.eq("brand_id",productQueryParam.getBrandId());
        }
        Page<Product> page = new Page<>(pageNum,pageSize);
        IPage<Product> selectPage = baseMapper.selectPage(page, queryWrapper);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",selectPage.getSize());
        map.put("totalPage",selectPage.getTotal());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());

        return map;
    }

    @Override
    public boolean updateProductByParam(Long id, PmsProductParam productParam) {
        ProductMapper baseMapper = getBaseMapper();
        Product product = baseMapper.selectById(id);


        /**
         *
         * @ApiModelProperty("商品阶梯价格设置")
        private List<ProductLadder> productLadderList;
         @ApiModelProperty("商品满减价格设置")
         private List<ProductFullReduction> productFullReductionList;
         @ApiModelProperty("商品会员价格设置")
         private List<MemberPrice> memberPriceList;
         @ApiModelProperty("商品的sku库存信息")
         private List<SkuStock> skuStockList;
         @ApiModelProperty("商品参数及自定义规格属性")
         private List<ProductAttributeValue> productAttributeValueList;
         @ApiModelProperty("专题和商品关系")
         private List<SubjectProductRelation> subjectProductRelationList;
         @ApiModelProperty("优选专区和商品的关系")
         private List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList;
         *
         */

        return false;
    }

    @Override
    public List<Product> listProductByNameOrSn(String keyword) {
        ProductMapper baseMapper = getBaseMapper();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>().like("name", keyword)
                .eq("product_sn", keyword);
        List<Product> productList = baseMapper.selectList(queryWrapper);
        return productList;
    }

    @Override
    public void addProduct(PmsProductParam productParam) {
        ProductMapper baseMapper = getBaseMapper();


    }

    @Override
    public void updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {

    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {

    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {

    }

    @Override
    public void updateNewStatus(List<Long> ids, Integer newStatus) {

    }

    @Override
    public void updateDeleteStatus(List<Long> ids, Integer deleteStatus) {

    }
}
