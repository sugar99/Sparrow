package com.sparrow.demo.utils;

import com.github.pagehelper.Page;
import lombok.Data;

/**
 * @author allen
 */

@Data
public class ResultModel<T> {
    /**
     * 请求结果
     * */
    private Boolean success;
    /**
     * 异常编码
     * */
    private Long errCode;
    /**
     * 异常名称
     * */
    private String errName;
    /**
     * 异常信息
     * */
    private String errMsg;
    /**
     * 总数
     * */
    private Long totalCount;
    /**
     * 页数
     * */
    private Integer pageNum;
    /**
     * 每页数量
     * */
    private Integer pageSize;
    /**
     * 业务信息
     * */
    private T datas;

    public ResultModel() {

    }

    public ResultModel(Boolean success, Long errCode, String errName, String errMsg, Long totalCount, Integer pageNum, Integer pageSize, T datas) {
        super();
        this.success = success;
        this.errCode = errCode;
        this.errName = errName;
        this.errMsg = errMsg;
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.datas = datas;
    }


    public static <T> ResultModel<T> success(T data) {
        return new ResultModel<>(true, 0L, "success", "success", null, null, null, data);
    }

    public static <T> ResultModel<T> success(long totalCount, T data) {
        return new ResultModel<>(true, 0L, "success", "success", totalCount, null, null, data);
    }

    public static <T> ResultModel<T> success(long totalCount, int pageNum, int pageSize, T data) {
        return new ResultModel<>(true, 0L, "success", "success", totalCount, pageNum, pageSize, data);
    }

    public static <T> ResultModel<Page<T>> successPage(Page<T> page) {
        return new ResultModel<Page<T>>(true, 0L, "success", "success", page.getTotal(), page.getPages(), page.getPageSize(), page);
    }

    public static ResultModel failure() {
        return new ResultModel<>(false, -1L, "failure", "error", null, null, null, null);
    }


    public static ResultModel failure(long errCode, String errName, String errMsg) {
        return new ResultModel<>(false, errCode, errName, errMsg, null, null, null, null);
    }

    public static ResultModel failure(String errMsg) {
        return new ResultModel<>(false, -1L, "failure", errMsg, null, null, null, null);
    }
}
