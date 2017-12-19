package com.lichi.increaselimit;

import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lichi.increaselimit.common.enums.ResultEnum;
import com.lichi.increaselimit.common.exception.BusinessException;
import com.lichi.increaselimit.common.utils.ResultVoUtil;
import com.lichi.increaselimit.common.utils.UploadUtils;
import com.lichi.increaselimit.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(description="文件上传")
@RestController
@RequestMapping(value = "/file")
@Slf4j
public class FileController {
	
	public static final String SHUIYIN = "?imageView2/1/w/600/h/400/q/75|watermark/2/text/6YeN5bqG6aqK6amw5paH5YyW/font/5b6u6L2v6ZuF6buR/fontsize/480/fill/I0ZGRkZGRg==/dissolve/100/gravity/SouthEast/dx/10/dy/10|imageslim";

	@ApiOperation(value = "文件上传")
	@RequestMapping(value = "upload",method=RequestMethod.POST)
	public ResultVo<Object> upload(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResultVoUtil.error(1, "文件内容为空");
		}
		// 获取文件名

		String fileName = file.getOriginalFilename();
		log.info("上传的文件名为：" + fileName);
		
		UploadUtils uploadUtils = new UploadUtils();
		String upload = null;
		try {
			upload = uploadUtils.upload(file, fileName);
			upload = "http://" + upload + SHUIYIN;
		} catch (IOException e) {
			throw new BusinessException(ResultEnum.FILE_UPLOAD_ERROR);
		}
		return ResultVoUtil.success(upload);
	}
}