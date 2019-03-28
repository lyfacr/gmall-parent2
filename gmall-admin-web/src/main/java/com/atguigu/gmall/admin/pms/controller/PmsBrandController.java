package com.atguigu.gmall.admin.pms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.pms.service.BrandService;
import com.atguigu.gmall.pms.vo.PmsBrandParam;
import com.atguigu.gmall.to.CommonResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 品牌功能Controller
 */
@CrossOrigin
@RestController
@Api(tags = "PmsBrandController",description = "商品品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {
    @Reference
    private BrandService brandService;

    @ApiOperation(value = "获取全部品牌列表")
    @GetMapping(value = "/listAll")
    public Object getList() {

        // 获取全部品牌列表  brandService.listAll()
        List<Brand> list = brandService.list();
        return new CommonResult().success(list);
    }

    @ApiOperation(value = "添加品牌")
    @PostMapping(value = "/create")
    public Object create(@Validated @RequestBody PmsBrandParam pmsBrand, BindingResult result) {
        CommonResult commonResult = new CommonResult();
        // 添加品牌
        boolean res= brandService.addBrand(pmsBrand);
        if(res){
            commonResult.success("新增品牌成功");
        }else {
            commonResult.failed().setMessage("新增失败");
        }
        return commonResult;
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable("id") Long id,
                              @Validated @RequestBody PmsBrandParam pmsBrandParam,
                              BindingResult result) {
        CommonResult commonResult = new CommonResult();

        // 更新品牌
        boolean res = brandService.updateBrand(id,pmsBrandParam);

        if(res){
            commonResult.success("更新品牌成功");
        }else {
            commonResult.failed().setMessage("更新失败");
        }
        return commonResult;
    }

    @ApiOperation(value = "删除品牌")
    @GetMapping(value = "/delete/{id}")
    public Object delete(@PathVariable("id") Long id) {
        CommonResult commonResult = new CommonResult();

        //删除品牌
        boolean res = brandService.removeById(id);
        if(res){
            commonResult.success("删除品牌成功");
        }else {
            commonResult.failed().setMessage("删除失败");
        }
        return commonResult;
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping(value = "/list")
    public Object getList(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        CommonResult commonResult = new CommonResult();

        // 根据品牌名称分页获取品牌列表
        Map<String,Object> brandPageInfo = brandService.pageBrand(keyword,pageNum,pageSize);

        return commonResult.success(brandPageInfo);
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable("id") Long id) {
        CommonResult commonResult = new CommonResult();
        // 根据编号查询品牌信息
        Brand res = brandService.getById(id);

        return commonResult.success(res);
    }

    @ApiOperation(value = "批量删除品牌")
    @PostMapping(value = "/delete/batch")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        CommonResult commonResult = new CommonResult();
        // 批量删除品牌
        boolean res = brandService.removeByIds(ids);

        if(res){
            commonResult = new CommonResult().success("删除品牌成功");
        }else {
            commonResult.failed().setMessage("删除失败");
        }
        return commonResult;
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping(value = "/update/showStatus")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        CommonResult commonResult = new CommonResult();
        // 批量更新显示状态
        brandService.updateshowStatus(ids,showStatus);

        return commonResult;
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping(value = "/update/factoryStatus")
    public Object updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                      @RequestParam("factoryStatus") Integer factoryStatus) {
        CommonResult commonResult = new CommonResult();
        // 批量更新厂家制造商状态
        brandService.updateFactoryStatus(ids,factoryStatus);

        return commonResult;
    }
}
