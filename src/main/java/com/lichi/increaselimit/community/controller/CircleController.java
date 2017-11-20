package com.lichi.increaselimit.community.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.community.controller.dto.CircleDto;
import com.lichi.increaselimit.community.entity.Circle;
import com.lichi.increaselimit.community.service.CircleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author by majie on 2017/11/15.
 */
@RestController
@Api(description = "圈子")
@RequestMapping("/circle")
@Slf4j
public class CircleController {

    @Autowired
    private CircleService circleService;

    @PostMapping
    @ApiOperation(value = "新建圈子")
    public ResultVo<Circle> postArticle(@Valid @RequestBody CircleDto circledto, BindingResult result) throws IOException{
    	log.info("新建圈子：{}" , circledto.getName());
        if(result.hasErrors()){
            String errors = result.getFieldError().getDefaultMessage();
            log.error("新建圈子参数错误：" + errors);
            return ResultVoUtil.error(1,errors);
        }
        Circle circle = new Circle();
        BeanUtils.copyProperties(circledto, circle);
		circleService.add(circle);
        return ResultVoUtil.success(circle);
    }

    @PutMapping
    @ApiOperation(value = "更新圈子信息")
    public ResultVo<Circle> update(CircleDto circledto){
    	log.info("更新圈子信息：{}" , circledto.getName());
    	Circle circle = circleService.get(circledto.getId());
        BeanUtils.copyProperties(circledto, circle);
        circleService.update(circle);
        return ResultVoUtil.success();

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除圈子")
    public ResultVo<Circle> deleteArticle(@PathVariable  Integer id){
    	log.info("删除圈子信息：id为{}" ,id);
        circleService.delete(id);
        return ResultVoUtil.success();
    }

    @GetMapping
    @ApiOperation(value = "分页查询列表")
    public ResultVo<Page<Circle>> getArticle(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "0",required = false) Integer page,
                                              @ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size){
    	Page<Circle> articles = circleService.getByPage(page,size);
        return ResultVoUtil.success(articles);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询圈子信息")
    public ResultVo<Circle> getArticle(@PathVariable  Integer id){
        Circle circle = circleService.get(id);
        return ResultVoUtil.success(circle);
    }


}
