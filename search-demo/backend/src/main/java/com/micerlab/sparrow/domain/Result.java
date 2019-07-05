package com.micerlab.sparrow.domain;

public class Result
{
    private int status; // 状态码
    private String msg; // 消息提示
    private Object data;
    
    public Result(int status, String msg)
    {
        this.status = status;
        this.msg = msg;
    }
    
    public Result(int status, String msg, Object data)
    {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public Object getData()
    {
        return data;
    }
    
    /**
     * 建议用ResultBuilder的build方法来生成Result
     * 这样Result的状态能够维持一致性：
     * --> 要么没有Result，
     * --> 要么Result的状态是不可改变的 immutable
     * 可以用 {@code Result.OK().msg("success").data(data).build();} 的流式API方式来构建Result
     */
    public static class ResultBuilder
    {
        private int status;
        private String msg;
        private Object data;
    
        public ResultBuilder(int status, String msg)
        {
            this.status = status;
            this.msg = msg;
        }
        
        public ResultBuilder msg(String msg)
        {
            this.msg = msg;
            return this;
        }
        
        public ResultBuilder data(Object data)
        {
            this.data = data;
            return this;
        }
        
        public Result build()
        {
            return new Result(status, msg, data);
        }
    
        public int getStatus()
        {
            return status;
        }
    
        public String getMsg()
        {
            return msg;
        }
    
        public Object getData()
        {
            return data;
        }
    }
    
    // 粗粒度地定义几种消息状态
    public static ResultBuilder OK() {return new ResultBuilder(200, "OK");}
    
    // 建议直接抛出业务异常
    @Deprecated
    public static ResultBuilder BadRequest() {return new ResultBuilder(400, "Bad Request");}
    
    @Deprecated
    public static ResultBuilder Forbidden() {return new ResultBuilder(403, "Forbidden");}
    
    @Deprecated
    public static ResultBuilder NotFound() {return new ResultBuilder(404, "Not Found");}
}
