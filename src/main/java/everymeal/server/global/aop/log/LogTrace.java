package everymeal.server.global.aop.log;


import everymeal.server.global.exception.ApplicationException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogTrace {

    private ThreadLocal<String> threadId = ThreadLocal.withInitial(this::createThreadId);

    public TraceInfo start(String method) {
        syncTrace();
        String id = threadId.get();
        long startTime = System.currentTimeMillis();
        logger().info("[" + id + "] " + method + " ==== start");
        return new TraceInfo(id, method, startTime);
    }

    public void end(TraceInfo traceInfo) {
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - traceInfo.startTime();
        if (resultTime >= 1000)
            logger().warn(
                            "["
                                    + traceInfo.theadId()
                                    + "] "
                                    + traceInfo.method()
                                    + " ==== execute time = "
                                    + resultTime
                                    + "ms");
        else
            logger().info(
                            "["
                                    + traceInfo.theadId()
                                    + "] "
                                    + traceInfo.method()
                                    + " ==== execute time = "
                                    + resultTime
                                    + "ms");
        removeThreadLocal();
    }

    public void apiException(ApplicationException e, TraceInfo traceInfo) {
        logger().error(
                        "["
                                + traceInfo.theadId()
                                + "] "
                                + traceInfo.method()
                                + " ==== API EXCEPTION! ["
                                + e.getErrorCode()
                                + "] "
                                + e.getMessage());
        removeThreadLocal();
    }

    public void exception(Exception e, TraceInfo traceInfo) {
        logger().error(
                        "["
                                + traceInfo.theadId()
                                + "] "
                                + traceInfo.method()
                                + " ==== INTERNAL ERROR! "
                                + e.getMessage());
        removeThreadLocal();
    }

    private void syncTrace() {
        String id = threadId.get();
        if (id == null) {
            threadId.set(createThreadId());
        }
    }

    private String createThreadId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void removeThreadLocal() {
        threadId.remove();
    }

    private Logger logger() {
        return LoggerFactory.getLogger(LogTrace.class);
    }
}
