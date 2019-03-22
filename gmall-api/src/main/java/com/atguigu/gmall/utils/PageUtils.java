package com.atguigu.gmall.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

public class PageUtils {

    public static Map<String,Object> getPageMap(IPage page){
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageSize",page.getSize());
        map.put("totalPage",page.getTotal());
        map.put("total",page.getTotal());
        map.put("pageNum",page.getCurrent());
        map.put("list",page.getRecords());
        return map;
    }


}
