package com.atguigu.gmall.cms.service;

import com.atguigu.gmall.cms.entity.PrefrenceArea;
import com.atguigu.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优选专区 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface PrefrenceAreaService extends IService<PrefrenceArea> {

    List<Product> listAllPrenceProduct();
}
