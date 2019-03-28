package com.atguigu.gmall.cms.service;

import com.atguigu.gmall.cms.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 专题表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface SubjectService extends IService<Subject> {

    Map<String,Object> pageInfoByKeyWord(String keyword, Integer pageNum, Integer pageSize);
}
