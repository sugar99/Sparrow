package com.micerlab.sparrow.utils;



import com.micerlab.sparrow.domain.ErrorCode;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil
{
    public static void setResponseStatus(HttpServletResponse response, ErrorCode errorCode)
    {
        response.setStatus(errorCode.getHttpStatus());
    }
}
