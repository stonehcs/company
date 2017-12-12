package com.lichi.increaselimit.course.controller;

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
import com.lichi.increaselimit.course.controller.dto.VideoDto;
import com.lichi.increaselimit.course.controller.dto.VideoUpdateDto;
import com.lichi.increaselimit.course.entity.Video;
import com.lichi.increaselimit.course.service.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/video")
@Api(description = "视频")
public class VideoController {

	@Autowired
	private VideoService videoService;

	@GetMapping
	@ApiOperation(value = "首页视频显示")
	public ResultVo<PageInfo<Video>> getVideoList(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		PageInfo<Video> list = videoService.getVideoList(page, size);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "查看视频详情")
	public ResultVo<Video> getVideo(@PathVariable Integer id) {
		Video Video = videoService.getVideo(id);
		return ResultVoUtil.success(Video);
	}

	@PostMapping
	@ApiOperation(value = "添加视频")
	public ResultVo<Video> addVideo(@Valid @RequestBody VideoDto VideoDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		Video Video = new Video();
		BeanUtils.copyProperties(VideoDto, Video);
		videoService.addVideo(Video);
		return ResultVoUtil.success();
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除视频")
	public ResultVo<Video> deleteVideo(@PathVariable Integer id) {
		videoService.deleteVideo(id);
		return ResultVoUtil.success();
	}

	@PutMapping
	@ApiOperation(value = "修改视频")
	public ResultVo<Video> updateVideo(@Valid @RequestBody VideoUpdateDto VideoDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			return ResultVoUtil.error(1, errors);
		}
		Video Video = new Video();
		BeanUtils.copyProperties(VideoDto, Video);
		videoService.updateVideo(Video);
		return ResultVoUtil.success();
	}

}
