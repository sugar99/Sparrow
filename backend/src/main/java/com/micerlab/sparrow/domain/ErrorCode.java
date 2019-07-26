package com.micerlab.sparrow.domain;

import com.micerlab.sparrow.domain.meta.FileType;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

/**
 * 自定义错误状态码
 * 规范设计，便于统一管理
 */
public enum ErrorCode
{
    // 400 BadRequest 参数错误等
    BAD_REQUEST_COMMON(400_000, "Bad Request"),
//    PARAM_ERR_SEARCH_TYPE(400_001, "search_type ∈ {all, image, doc, video, audio, others}"),
    PARAM_ERR_SEARCH_TYPE(400_001, "search_type ∈ " + Arrays.asList(FileType.values()).toString()),
    PARAM_ERR_FILTER_TYPE(400_002, "filter_type ∈ {tag, category}"),
    PARAM_ERR_FILTER_TYPES(400_003, "filter_types ∈ {tags, categories}"),
    PARAM_ERR_REQUEST_DATA_FIELD_UNPASS(400_001, "请求数据字段验证不通过"),
    PARAM_ERR_REQUEST_DATA_REQUIRED_FIELD_IS_NULL(400_002, "请求数据必须字段不可为空"),
    PARAM_ERR_PARENT_ID_NOT_IN_DOC(400_002, "父版本文件不在指定文档下"),
    
    
    // 403 Forbidden 权限：未授权 / 非法访问
    FORBIDDEN_COMMON(403_000, "Forbidden"),
    FORBIDDEN_NO_WRITE_CUR_DIR(403_001, "用户对当前目录没有可写权限，无法进行该操作"),
    FORBIDDEN_NO_READ_CUR_DIR(403_002, "用户对当前目录没有可读权限，无法进行该操作"),
    FORBIDDEN_NO_WRITE_CUR_DOC(403_003, "用户对当前文档没有可写权限，无法进行该操作"),
    FORBIDDEN_NO_READ_CUR_DOC(403_004, "用户对当前文档没有可读权限，无法进行该操作"),
    FORBIDDEN_NO_READ_TARGET_RESOURCE(403_005, "用户对目标资源没有可读权限，无法进行该操作"),
    FORBIDDEN_NOT_GROUP_OWNER(403_006, "只有群主才有权限进行该操作"),
    FORBIDDEN_NOT_GROUP_MEMBER(403_007, "只有群成员才有权限进行该操作"),
    FORBIDDEN_NOT_RESOURCE_OWNER(403_007, "只有资源的创建者才有权限进行该操作"),
    // 404 Not Found
    NOT_FOUND_COMMON(404_000, "Not Found"),
    NOT_FOUND_USERNAME_OR_PASSWORD_INVALID(404_001, "用户不存在或密码错误"),
    NOT_FOUND_FILE_ID(404_002, "文件id不存在"),
    NOT_FOUND_DOC_ID(404_002, "文档id不存在"),
    NOT_FOUND_TAG_ID(404_002, "标签id不存在"),
    NOT_FOUND_CATEGORY_ID(404_002, "类目id不存在"),
    NOT_FOUND_FILE_IN_DOC(400_002, "文件不在指定文档内"),
    
    
    // 500 Internal Server Error 服务器错误
    SERVER_EXCEPTION(500_000, "服务器发生异常"),
    SERVER_ERR_DB(500_001, "数据库异常"),
    SERVER_ERR_ELASTICSEARCH(500_002, "Elasticsearch异常"),
    SERVER_ERR_OSS(500_003, "OSS错误调用"),
    SERVER_ERR_Minio(500_004, "Minio异常"),
    SERVER_ERR_RABBITMQ(500_005, "rabbitmq异常")


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
