package com.lichi.increaselimit.common.enums;

import lombok.Getter;

/**
 * 返回状态和参数的枚举
 * @author majie
 *
 */
@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    ARTICLE_NO_EMPTY(2, "帖子数量不为空，不能删除该圈子"),

    CIRCLE_NO_EXIST(3, "圈子不存在"),
    
    CIRCLE_ID_NOT_EXIST(3, "请选择圈子"),
    LOCATION_ID_NOT_EXIST(3, "请选择区域"),
    
    CIRCLE_HAS_EXIST(4, "圈子已存在"),
    
    COURSE_NOT_EMPTY(4, "该讲师有对应课程，不能删除"),
    
    FILE_UPLOAD_ERROR(4, "文件上传异常"),
    
    NOT_SUPPORT_ERROR(5, "不支持该银行卡类型"),
    
    MOBILE_NUM_EMPTY(5, "该手机号用户不存在"),
    
    MOBILE_ERROR(5, "请输入正确的手机号码"),
    
    MOBILE_EXIST(5, "该手机号已被绑定"),
    
    CODE_EXIST(5, "请一分钟后再尝试"),
    
    CODE_NOT_CHOICE(5, "请出入验证码"),
    
    CODE_NOT_EXIST(5, "验证码不存在"),
    
    VALIDATECODE_ERROR(5, "请填写正确的验证码"),
    
    VALIDATECODE_TIMEOUT(5, "验证码已失效"),

    LOGIN_FAIL(25, "登录失败, 登录信息不正确"),
    
    REGISTER_ERROR(25, "注册用户失败"),
    
    CONTENT_ERRO(25, "帖子内容异常"),
    
    ENDTIME_BIGGER_THEN_STARTTIME(25, "结束时间应该大于开课时间"),
    
//    PASSWORD_NOT_CHANGE(25, "注册用户失败"),

    LOGOUT_SUCCESS(26, "登出成功"),
    
    TOKEN_EXPIRED(26, "token已失效"),
    
    NO_RESPONSE(26, "服务繁忙"),
    
    EMAIL_EXSIT(26, "邮箱已存在"),
    
    NO_ONLINE_USER(26, "没有在线的客服"),
    
    LOGIN_TIME_OUT(26, "登录超时,请重新登录"),
    
    COURSE_HAS_SIGNUP(26, "该课程已报名"),
    COURSE_HAS_PAY(26, "该课程已付费"),
    SEND_SMS_ERROR(25, "短信系统异常"),
    
    BILL_NOT_EXIST(25, "该邮箱内没有可用账单"),
    
    NOT_ALLOWED_METHOD(25, "内测阶段不允许第三方登录"), 
    
    USERID_NOT_CHOICE(25, "请传入用户id"),
    
    BANKNAME_NOT_CHOICE(25, "请传入银行名称"), 
    
    LAST4_NOT_CHOICE(25, "请传入银行卡后四位"),
    
    EMAIL_NOT_CHOICE(25, "请输入邮箱"),
    EMAIL_PASSWORD_NOT_CHOICE(25, "请输入邮箱密码"), 
    
    STATUS_NOT_CHOICE(25, "请选择状态"),
    HAVE_NO_AUTH(25, "自动账单不能修改"),
    TIME_NOT_CORRECT(25, "请选择正确的日期"),
    
    
    
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
