package com.atguigu.gmall.ums.service.impl;

import com.atguigu.gmall.ums.entity.MemberLevel;
import com.atguigu.gmall.ums.mapper.MemberLevelMapper;
import com.atguigu.gmall.ums.service.MemberLevelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {
    @Override
    public List<MemberLevel> getMemberLevelByStatus(Integer defaultStatus) {
        MemberLevelMapper baseMapper = getBaseMapper();
        List<MemberLevel> memberList = baseMapper.selectList(new QueryWrapper<MemberLevel>().eq("default_status", defaultStatus));
        return memberList;
    }
}
