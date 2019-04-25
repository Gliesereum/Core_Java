package com.gliesereum.share.common.logging.appender;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import com.gliesereum.share.common.logging.service.LoggingService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 */
@Setter
@Getter
@Component
public class LoggingAppender extends AppenderBase<ILoggingEvent> {

    private PatternLayout layout;

    private List<Map<String,Object>> eventQueue = new LinkedList<>();

    public static LoggingService loggingService;

    private static boolean publisherEnable = false;

    private static boolean queueIsNotEmpty = false;

    private static volatile boolean remoteLoggingEnable = true;

    private static volatile LoggingAppender activeAppender;

    @Autowired
    public void setPublisher(LoggingService loggingService,
                             @Value("${remoteLogging.enable:true}") Boolean remoteLoggingEnable) {
        if (remoteLoggingEnable) {
            this.loggingService = loggingService;
            publisherEnable = true;
        } else {
            if(activeAppender != null) {
                activeAppender.stop();
            }
            this.remoteLoggingEnable = false;
            eventQueue.clear();
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (remoteLoggingEnable) {
            Map<String, Object> eventMap = new HashMap<>();
            eventMap.put("timestamp", event.getTimeStamp());
            eventMap.put("level", event.getLevel());
            eventMap.put("thread", event.getThreadName());
            eventMap.put("logger", event.getLoggerName());
            eventMap.put("massage", event.getFormattedMessage());
            eventMap.put("mdc", event.getMDCPropertyMap());
            putError(eventMap, event);
            if (publisherEnable) {
                if (queueIsNotEmpty) {
                    eventQueue.forEach(loggingService::publishingObject);
                    eventQueue.clear();
                    queueIsNotEmpty = false;
                }
                loggingService.publishingObject(eventMap);
            } else {
                eventQueue.add(eventMap);
                queueIsNotEmpty = true;
            }
        }
    }

    @Override
    public void start() {
        super.start();
        activeAppender = this;
    }

    private void putError(Map<String, Object> eventMap, ILoggingEvent event) {
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if(throwableProxy != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(throwableProxy.getMessage());
            builder.append("\n");
            StackTraceElementProxy[] stackTrace = throwableProxy.getStackTraceElementProxyArray();
            if (stackTrace != null) {
                for (StackTraceElementProxy element : stackTrace) {
                    builder.append(element.getSTEAsString());
                    builder.append("\n");
                }

            }
            eventMap.put("stackTrace", builder.toString());
        }
    }

}
