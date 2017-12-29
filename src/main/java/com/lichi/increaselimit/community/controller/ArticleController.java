package com.lichi.increaselimit.community.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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
import com.lichi.increaselimit.community.entity.ArticleVo;
import com.lichi.increaselimit.community.entity.CircleArticle;
import com.lichi.increaselimit.community.service.ArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author  by majie on 2017/11/15.
 */
@RestController
@Api(description = "帖子")
@RequestMapping("/circle/article")
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @ApiOperation(value = "发帖")
    public ResultVo<Article> postArticle(@Valid @RequestBody ArticleDto articledto, BindingResult result){
        if(result.hasErrors()){
            String errors = result.getFieldError().getDefaultMessage();
            log.warn("发帖参数错误：" + errors);
            return ResultVoUtil.error(1,errors);
        }
        log.info("发帖,帖子标题:{}",articledto.getTitle());
        Article article = new Article();
        BeanUtils.copyProperties(articledto,article);
        articleService.add(article);
        return ResultVoUtil.success();
    }
    
    
	@PutMapping
	@ApiOperation(value = "更新帖子")
	public ResultVo<Article> update(@Valid @RequestBody ArticleUpdateDto articledto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			log.warn("修改帖子参数错误：" + errors);
			return ResultVoUtil.error(1, errors);
		}
		log.info("更新帖子,帖子id:{}",articledto.getId());
		Article article = new Article();
		BeanUtils.copyProperties(articledto, article);
		articleService.update(article);
		return ResultVoUtil.success();

	}

    @GetMapping
    @ApiOperation(value = "分页查询列表")
    public ResultVo<PageInfo<ArticleVo>> getArticle(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "1",required = false) Integer page,
                                              @ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size,
                                              @ApiParam(value = "圈子id",required = true) @RequestParam Integer circleId){
    	log.info("分页查询对应圈子下帖子列表,圈子id:{}",circleId);
    	PageInfo<ArticleVo> articles = articleService.getByPage(page,size,circleId);
        return ResultVoUtil.success(articles);

    }
    
    @GetMapping("/hot")
    @ApiOperation(value = "查询热门帖子")
    public ResultVo<PageInfo<ArticleVo>> getHotArticle(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "1",required = false) Integer page,
                                              @ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size){
    	log.info("查询热门帖子");
    	PageInfo<ArticleVo> articles = articleService.getHotByPage(page,size);
        return ResultVoUtil.success(articles);

    }
    
    @GetMapping("/index-article")
    @ApiOperation(value = "查询首页帖子信息")
    public ResultVo<PageInfo<CircleArticle>> getList(@ApiParam(value = "页码",required = false) @RequestParam(defaultValue = "1",required = false) Integer page,
    		@ApiParam(value = "条数",required = false) @RequestParam(defaultValue = "20",required = false) Integer size){
    	log.info("查询首页帖子信息");
    	PageInfo<CircleArticle> articles = articleService.getIndexArticle(page,size);
    	return ResultVoUtil.success(articles);
    	
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询帖子")
    public ResultVo<Article> getArticle(@PathVariable  Integer id){
    	log.info("查询id:{}对应的帖子信息",id);
        Article article = articleService.get(id);
        return ResultVoUtil.success(article);

    }


}
