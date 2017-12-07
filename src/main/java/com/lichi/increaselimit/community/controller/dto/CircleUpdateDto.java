package com.lichi.increaselimit.community.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 圈子dto
 * @author by majie on 2017/11/16.
 */
@Data
@ApiModel("圈子更新 ")
public class CircleUpdateDto implements Serializable{

	private static final long serialVersionUID = 8344002416206506566L;
	
	@ApiModelProperty(value = "圈子id,必填")
	@NotNull(message = "圈子id不能为空")
    private Integer id;

    @ApiModelProperty(value = "圈子名称")
    private String name;

    @ApiModelProperty(value = "头像地址")
    private String url;
    
    @ApiModelProperty(value = "圈子描述")
    private String introduce;
    
    @ApiModelProperty(value = "所有圈子排序")
    private Integer sort1;
    
    @ApiModelProperty(value = "热门圈子排序")
    private Integer sort2;

}
