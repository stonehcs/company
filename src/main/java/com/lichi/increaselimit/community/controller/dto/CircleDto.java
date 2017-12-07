package com.lichi.increaselimit.community.controller.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 圈子dto
 * @author by majie on 2017/11/16.
 */
@Data
@ApiModel("圈子 ")
public class CircleDto implements Serializable{

	private static final long serialVersionUID = 8344002416206506566L;
	
    @ApiModelProperty(value = "圈子名称")
    @NotBlank(message = "圈子名字不能为空")
    private String name;

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String createUserId;

    @ApiModelProperty(value = "头像地址")
    private String url;
    
    @ApiModelProperty(value = "圈子描述")
    private String introduce;
    
    @ApiModelProperty(value = "所有圈子排序，可以为空")
    private Integer sort1;
    
    @ApiModelProperty(value = "热门圈子排序，可以为空")
    private Integer sort2;

}
