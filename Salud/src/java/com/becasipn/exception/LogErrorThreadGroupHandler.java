package com.becasipn.exception;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.commons.CommonsLogger;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.LogManager;

public class LogErrorThreadGroupHandler extends ThreadGroup {

    private final Logger LOG = new CommonsLogger(new Log4JLogger(LogManager.getLogger(this.getClass().getName())));

    public LogErrorThreadGroupHandler() {
        super(LogErrorThreadGroupHandler.class.getSimpleName());
    }

    public LogErrorThreadGroupHandler(String name) {
        super(name);
    }

    /**
     * Solo registra en el log como <b>ERROR</b> la excepción.
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LOG.error(thread.getName(), throwable);
    }
}
