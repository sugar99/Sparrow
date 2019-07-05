package com.micerlab.sparrow.domain;

import org.springframework.http.HttpStatus;

/**
 * 自定义错误状态码
 * 规范设计，便于统一管理
 */
public enum ErrorCode
{
    // 400 BadRequest 参数错误等
    BAD_REQUEST_COMMON(400_000, "Bad Request"),
    PARAM_ERR_REQUEST_DATA_FIELD_UNPASS(400_001, "请求数据字段验证不通过"),
    PARAM_ERR_REQUEST_DATA_REQUIRED_FIELD_IS_NULL(400_002, "请求数据必须字段不可为空"),
    PARAM_ERR_USERNAME_REGISTERED_EXISTS(400_003, "注册用户名已存在"),
    PARAM_ERR_FAVORITE_TYPE(400_004, "favorite_type ∈ {author, book}"),
    
    
    // 403 Forbidden 权限：未授权 / 非法访问
    FORBIDDEN_COMMON(403_000, "Forbidden"),
    
    // 404 Not Found
    NOT_FOUND_COMMON(404_000, "Not Found"),
    NOT_FOUND_USERNAME_OR_PASSWORD_INVALID(404_001, "用户不存在或密码错误"),
    NOT_FOUND_BOOK_ID(404_002, "图书id不存在"),
    NOT_FOUND_COLLECTION(404_003, "用户不存在，或图书不存在，或用户未收藏图书"),
    NOT_FOUND_FAVORITE(404_004, "用户不存在，或图书（作者）不存在，或用户未收藏图书（作者）"),
    
    
    
    // 500 Internal Server Error 服务器错误
    SERVER_EXCEPTION(500_000, "服务器发生异常"),
    SERVER_ERROR_DB(500_001, "数据库异常"),
    SERVER_ERROR_ELASTICSEARCH(500_002, "Elasticsearch异常"),
    SERVER_ERROR_NEO4J(500_003, "Neo4j异常"),
    
    
    ;
    
    private final int status;
    private final String msg;
    
    ErrorCode(int status, String msg)
    {
        this.status = status;
        this.msg = msg;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    /**
     * 获取相应的http状态码，用于设置返回response的status
     * @return 3位 http status
     */
    public int getHttpStatus()
    {
        HttpStatus status = HttpStatus.valueOf(getStatus() / 1000);
        return status.value();
    }
}
