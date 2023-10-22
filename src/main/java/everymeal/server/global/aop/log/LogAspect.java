package everymeal.server.global.aop.log;


import everymeal.server.global.exception.ApplicationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private LogTrace logTrace;

    @Autowired
    public LogAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("everymeal.server.global.aop.log.Pointcuts.allService()")
    public Object executingTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceInfo traceInfo = null;
        try {
            traceInfo = logTrace.start(joinPoint.getSignature().toShortString());
            Object result = joinPoint.proceed();
            logTrace.end(traceInfo);
            return result;
        } catch (ApplicationException e) {
            if (traceInfo != null) {
                logTrace.apiException(e, traceInfo);
            }
            throw e;
        } catch (Exception e) {
            if (traceInfo != null) {
                logTrace.exception(e, traceInfo);
            }
            throw e;
        }
    }
}
