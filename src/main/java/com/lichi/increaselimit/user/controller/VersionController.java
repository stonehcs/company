package com.lichi.increaselimit.user.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.vo.ResultVo;
import com.lichi.increaselimit.user.controller.dto.VersionDto;
import com.lichi.increaselimit.user.entity.Version;
import com.lichi.increaselimit.user.service.VersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api(description = "版本信息")
@RestController
@RequestMapping("/version")
@Slf4j
public class VersionController {

	@Autowired
	private VersionService versionService;

	@GetMapping
	@ApiOperation("分页查询所有版本")
	public ResultVo<PageInfo<Version>> getAll(
			@ApiParam(value = "页码", required = false) @RequestParam(defaultValue = "1", required = false) Integer page,
			@ApiParam(value = "条数", required = false) @RequestParam(defaultValue = "20", required = false) Integer size) {
		log.info("分页查询所有版本");
		PageInfo<Version> list = versionService.selectAll(page, size);
		return ResultVoUtil.success(list);
	}
	@GetMapping("/new")
	@ApiOperation("获得最新版本")
	public ResultVo<Version> getAll() {
		log.info("查询所有版本");
		Version list = versionService.selectNew();
		return ResultVoUtil.success(list);
	}

	@PostMapping
	@ApiOperation("添加版本信息")
	public ResultVo<Version> add(@Valid @RequestBody VersionDto deptDto, BindingResult result) {
		if (result.hasErrors()) {
			String errors = result.getFieldError().getDefaultMessage();
			log.warn("添加版本信息参数错误:{}" + errors);
			return ResultVoUtil.error(1, errors);
		}
		log.info("添加版本,版本号:{}",deptDto.getVersion());
		Version dept = new Version();
		BeanUtils.copyProperties(deptDto, dept);
		versionService.insertOne(dept);
		return ResultVoUtil.success();
	}


	@DeleteMapping("/{id}")
	@ApiOperation("根据id删除版本信息")
	public ResultVo<Version> delete(@PathVariable Integer id) {
		log.info("删除版本信息,版本id:{}",id);
		versionService.deleteOne(id);
		return ResultVoUtil.success();
	}

	@GetMapping("/{id}")
	@ApiOperation("根据id查询版本信息")
	public ResultVo<Version> get(@PathVariable Integer id) {
		log.info("查询版本信息,版本id:{}",id);
		Version list = versionService.selectOne(id);
		return ResultVoUtil.success(list);
	}
}
