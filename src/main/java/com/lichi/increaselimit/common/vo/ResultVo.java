package com.lichi.increaselimit.common.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * http请求返回的最外层对象
 * @author majie
 *
 * @param <T>
 */
@Data
public class ResultVo<T> implements Serializable{

	private static final long serialVersionUID = 4339818104919790459L;

	/** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
