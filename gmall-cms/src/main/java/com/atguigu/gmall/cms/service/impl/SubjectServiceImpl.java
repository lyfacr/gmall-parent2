package com.atguigu.gmall.cms.service.impl;

import com.atguigu.gmall.cms.entity.Subject;
import com.atguigu.gmall.cms.mapper.SubjectMapper;
import com.atguigu.gmall.cms.service.SubjectService;
import com.atguigu.gmall.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public Map<String, Object> pageInfoByKeyWord(String keyword, Integer pageNum, Integer pageSize) {
        SubjectMapper baseMapper = getBaseMapper();
        QueryWrapper<Subject> queryWrapper = null;
        if(!StringUtils.isEmpty(keyword)){
            queryWrapper = new QueryWrapper<Subject>().eq("title", keyword);
        }
        IPage<Subject> selectPage = baseMapper.selectPage(new Page<Subject>(pageNum, pageSize), queryWrapper);
        Map<String, Object> map = PageUtils.getPageMap(selectPage);
        return map;
    }
}
