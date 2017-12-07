package com.lichi.increaselimit.community.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.community.controller.dto.ArticleDto;
import com.lichi.increaselimit.community.controller.dto.ArticleUpdateDto;
import com.lichi.increaselimit.community.entity.Article;
import com.lichi.increaselimit.community.service.ArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author  by majie on 2017/11/15.
 */
@RestController
@Api(description = "帖子")
@RequestMapping("/circle/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @ApiOperation(value = "发帖")
    public ResultVo<Article> postArticle(@Valid @RequestBody ArticleDto articledto, BindingResult result){
        if(result.hasErrors()){
            String errors = result.getFieldError().getDefaultMessage();
            return ResultVoUtil.error(1,errors);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articledto,article);
        articleService.add(article);
        return ResultVoUtil.success();
    }

    @PutMapping
    @ApiOperation(value = "更新帖子")
    public ResultVo<Article> update(@Valid ArticleUpdateDto articledto, BindingResult result){
        if(result.hasErrors()){
            String errors = result.getFieldError().getDefaultMessage();
            return ResultVoUtil.error(1,errors);
        }
    	Article article = new Article();
    	BeanUtils.copyProperties(articledto,article);
        articleService.update(article);
        return ResultVoUtil.success();

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除帖子")
    public ResultVo<Article> deleteArticle(@PathVariable  Integer id){
        articleService.delete(id);
        return ResultVoUtil.success();
    }

    @GetMapping
    @ApiOperation(value = "分页查询列表")
    public ResultVo<PageInfo<Article>> getArticle(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "1",required = false) Integer page,
                                              @ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size,
                                              @ApiParam(value = "圈子id",required = true) @RequestParam Integer circleId){
    	PageInfo<Article> articles = articleService.getByPage(page,size,circleId);
        return ResultVoUtil.success(articles);

    }
    
    @GetMapping("/hot")
    @ApiOperation(value = "查询热门帖子")
    public ResultVo<PageInfo<Article>> getHotArticle(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "1",required = false) Integer page,
                                              @ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size){
        PageInfo<Article> articles = articleService.getHotByPage(page,size);
        return ResultVoUtil.success(articles);

    }

    @GetMapping("/{id}")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "token", required = true, dataType = "string", paramType = "header",defaultValue="bearer ")})
    @ApiOperation(value = "根据id查询帖子")
    public ResultVo<Article> getArticle(@PathVariable  Integer id){
        Article article = articleService.get(id);
        return ResultVoUtil.success(article);

    }


}
