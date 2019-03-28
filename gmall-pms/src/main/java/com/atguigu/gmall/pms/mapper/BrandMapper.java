package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface BrandMapper extends BaseMapper<Brand> {

    void updateBatchShowStatus(@Param("ids") List<Long> ids, Integer showStatus);

    void updateBatchFactoryStatus(@Param("ids")List<Long> ids, Integer factoryStatus);
}
