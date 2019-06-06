package com.sparrow.demo.service.callback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author allen
 */
public interface CallbackService {
    void doPolicyGet(HttpServletRequest request, HttpServletResponse response);

    void doCallbackPost(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
