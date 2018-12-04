package com.becasipn.util;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import java.util.Date;

/**
 * Interceptor generado para el manejo de Excepciones
 *
 * @author Patricia Benitez
 * @version $Rev: 1169 $
 * @since 1.0
 */
public class ExceptionMappingInterceptor extends com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor {

    private static final long serialVersionUID = 4818267512512634814L;

    @Override
    protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
        LOG.error("Publishing Exception...");
        java.util.Date date = new Date();
        String time = (new Long(date.getTime())).toString();
        StringBuffer strBuffer = new StringBuffer();
        for (StackTraceElement ste : exceptionHolder.getException().getStackTrace()) {
            strBuffer.append("\t" + ste.getClassName() + "/" + ste.getMethodName() + ":" + ste.getLineNumber() + "\n");
        }
        LOG.error(time + ": " + exceptionHolder.getException().getMessage());
        LOG.error(strBuffer.toString());
        exceptionHolder.getException().printStackTrace();
        ExceptionHolder eh = new ExceptionHolder(new Exception(time + "  " + "", exceptionHolder.getException()));

        invocation.getStack().push(eh);
    }

}
