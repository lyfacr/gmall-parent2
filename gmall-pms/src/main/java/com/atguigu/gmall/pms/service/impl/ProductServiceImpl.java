package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.mapper.*;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.atguigu.gmall.pms.vo.PmsProductResult;
import com.atguigu.gmall.search.GmallSearchService;
import com.atguigu.gmall.to.es.EsProduct;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductLadderMapper productLadderMapper;

    @Autowired
    ProductFullReductionMapper productFullReductionMapper;

    @Autowired
    MemberPriceMapper memberPriceMapper;

    @Autowired
    SkuStockMapper skuStockMapper;

    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    JedisPool jedisPool;

    @Reference(version = "1.0")
    GmallSearchService searchService;

    ThreadLocal<Product> local = new ThreadLocal<>();


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

    /**
     * 修改商品
     * @param id
     * @param productParam
     * @return
     */
    @Override
    public boolean updateProductByParam(Long id, PmsProductParam productParam) {
        ProductMapper baseMapper = getBaseMapper();
        Product product = baseMapper.selectById(id);
        //TODO


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

    /**
     * 添加商品
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addProduct(PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl)AopContext.currentProxy();

        //1保存sku和spu
        psProxy.saveBaseProductInfo(productParam);

        //2.商品阶梯价格设置
        psProxy.saveProductLadder(productParam.getProductLadderList());
        //3.商品满减价格设置
        psProxy.saveProductFullReduction(productParam.getProductFullReductionList());
        //4.商品会员价格设置
        psProxy.saveMemberPrice(productParam.getMemberPriceList());

        //6.商品参数及自定义规格属性
        psProxy.saveProductAttributeValue(productParam.getProductAttributeValueList());
        //7.更新商品分类数目
        psProxy.updateProductCategoryCount();

    }
    //1.保存商品基本信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProduct(PmsProductParam productParam){
        Product product = new Product();
        BeanUtils.copyProperties(productParam,product);
        productMapper.insert(product);
        //将基本信息存入线程共享
        local.set(product);
    }
    //2.商品阶梯价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadder(List<ProductLadder> list){
        Product product = local.get();
        list.forEach(pld->{
            pld.setProductId(product.getId());
            productLadderMapper.insert(pld);
        });
    }
    //3.商品满减价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductFullReduction(List<ProductFullReduction> list){
        Product product = local.get();
        list.forEach(pfn->{
            pfn.setProductId(product.getId());
            productFullReductionMapper.insert(pfn);
        });
    }
    //4.商品会员价格设置
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMemberPrice(List<MemberPrice> list){
        Product product = local.get();
        list.forEach(mp->{
            mp.setProductId(product.getId());
            memberPriceMapper.insert(mp);
        });
    }
    //5.商品的sku库存信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSkuStock(List<SkuStock> list){
        Product product = local.get();
        AtomicReference<Integer> i = new AtomicReference<>(0);
        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        numberFormat.setMaximumIntegerDigits(2);
        numberFormat.setMinimumIntegerDigits(2);
        list.forEach(sku->{
            sku.setProductId(product.getId());
            i.set(i.get()+1);
            String format = numberFormat.format(i.get());
            String code = "K_"+product.getId()+"_"+format;
            sku.setSkuCode(code);
            skuStockMapper.insert(sku);
        });
    }
    //6.商品参数及自定义规格属性
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValue(List<ProductAttributeValue> list){
        Product product = local.get();
        list.forEach(pav->{
            pav.setProductId(product.getId());
            productAttributeValueMapper.insert(pav);
        });
    }
    //7.更新商品分类数目
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductCategoryCount(){
        Product product = local.get();
        Long id = product.getProductCategoryId();

        productCategoryMapper.updateCountById(id);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseProductInfo(PmsProductParam productParam){
        ProductServiceImpl psProxy = (ProductServiceImpl)AopContext.currentProxy();
        //1.保存商品基本信息
        psProxy.saveProduct(productParam);
        //5.商品的sku库存信息
        psProxy.saveSkuStock(productParam.getSkuStockList());
    }


    @Override
    public void updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        // 批量修改审核状态
//        ProductMapper baseMapper = getBaseMapper();
//        baseMapper.updatebatchVerifyStatus(ids,verifyStatus,detail);
    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {
        // 批量上下架
//        ProductMapper baseMapper = getBaseMapper();
//        baseMapper.updatebatchPublishStatus(ids,publishStatus);
        if(publishStatus == 1){
            publishProduct(ids);
        }else{
            removeProduct(ids);
        }
    }

    private void publishProduct(List<Long> ids){
        //1.查询当前上架的商品的sku信息和spu信息
        ids.forEach(id->{
            //1)spu
            Product product = productMapper.selectById(id);
            //2)sku
            List<SkuStock> skuStocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", product.getId()));
            //3)这个商品所有参数值
            List<EsProductAttributeValue> attributeValues = productAttributeValueMapper.selectProductAttrValues(product.getId());
            //4)改写信息 将其发布到es 统计上架状态是否全部完成
            AtomicReference<Integer> count = new AtomicReference<Integer>(0);

            skuStocks.forEach(sku->{
                EsProduct esProduct = new EsProduct();
                BeanUtils.copyProperties(product,esProduct);
                //5)改写商品的标题，加上sku的销售属性
                esProduct.setName(product.getName()+" "+sku.getSp1()+" "+sku.getSp2()+" "+sku.getSp3());
                esProduct.setPrice(sku.getPrice());
                esProduct.setStock(sku.getStock());
                esProduct.setSale(sku.getSale());
                esProduct.setAttrValueList(attributeValues);
                //6)改写id使用sku的id
                esProduct.setId(sku.getId());
                //7)保存到es中 5个成了3个败了 不成
                boolean es = searchService.saveProductInfoToES(esProduct);
                count.set(count.get()+1);
                if(es){
                    //保存当前的id,list.add(id)
                }
            });
            //8)判断是否完全上架成功，成功改数据库状态
            if(count.get()==skuStocks.size()){
                //9)修改数据库状态都是包装类型允许null值
                Product update = new Product();
                update.setId(product.getId());
                update.setPublishStatus(1);
                productMapper.updateById(update);
            }else {
                //成功的撤销操作来保证业务数据的一致性
                //es有失败 list.forEach(remove());
            }
        });
    }
    private void removeProduct(List<Long> ids){

    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        // 批量推荐商品
        ProductMapper baseMapper = getBaseMapper();
        baseMapper.updateBatchRecommendStatus(ids,recommendStatus);

    }

    @Override
    public void updateNewStatus(List<Long> ids, Integer newStatus) {
        // 批量设为新品
        ProductMapper baseMapper = getBaseMapper();
        baseMapper.updateBatchNewStatus(ids,newStatus);
    }

    @Override
    public void updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        //批量修改删除状态
        ProductMapper baseMapper = getBaseMapper();
        baseMapper.updateBatchDeleteStatus(ids,deleteStatus);
    }

    //查询销售属性的列表
    @Override
    public List<EsProductAttributeValue> getProductSaleAttr(Long productId) {

        return productMapper.getProductSaleAttr(productId);
    }
    //查询基本属性值的列表
    @Override
    public List<EsProductAttributeValue> getProductBaseAttr(Long productId) {
        return productMapper.getProductBaseAttr(productId);
    }

    @Override
    public Product getProductByIdFromCache(Long productId) {
        Jedis jedis = jedisPool.getResource();
        //1.先去缓存中检索 GULI:PRODUCT:INFO
        Product product = null;
        String s = jedis.get(RedisCacheConstant.PRODUCT_INFO_CACHE_KEY + productId);
        if(StringUtils.isEmpty(s)){
            //2.缓存中没有去找数据库
            //3.去redis中占坑
            Long lock = jedis.setnx("lock", "123");
            if(lock == 1){
                //获取到锁 查数据 放在缓存中
                product = productMapper.selectById(productId);
                //finally releaseLock();
                //if(product!=null)
                //3.放入缓存
                String json = JSON.toJSONString(product);//无论数据库是否有值都应该放 防止穿透
                if(product == null){
                    int anInt = new Random().nextInt(2000);
                    jedis.setex(RedisCacheConstant.PRODUCT_INFO_CACHE_KEY+productId,60+anInt,json);
                }else {
                    //过期时间
                    int anInt = new Random().nextInt(2000);
                    jedis.setex(RedisCacheConstant.PRODUCT_INFO_CACHE_KEY+productId,60*60*24*3+anInt,json);
                }
                jedis.del("lock");
            }else {
                try {
                    Thread.sleep(1000);
                    //如果没有获取去查数据库,我们等待一会,再去缓存看
                    getProductByIdFromCache(productId);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            //4.缓存中有
            product = JSON.parseObject(s, Product.class);
        }
        jedis.close();
        return product;
    }


}
