package everymeal.server.global.aop.log;

public record TraceInfo(String theadId, String method, Long startTime) {}
