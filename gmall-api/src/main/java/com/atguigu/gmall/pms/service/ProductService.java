package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.atguigu.gmall.pms.vo.PmsProductResult;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductService extends IService<Product> {

    Map<String,Object> pageInfo(Integer pageSize, Integer pageNum,PmsProductQueryParam productQueryParam);

    boolean updateProductByParam(Long id,PmsProductParam productParam);

    List<Product> listProductByNameOrSn(String keyword);


    void addProduct(PmsProductParam productParam);

    void updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    void updateNewStatus(List<Long> ids, Integer newStatus);

    void updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    List<EsProductAttributeValue> getProductSaleAttr(Long productId);

    List<EsProductAttributeValue> getProductBaseAttr(Long productId);

    Product getProductByIdFromCache(Long productId);
}
