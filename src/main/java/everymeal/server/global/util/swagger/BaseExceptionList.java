package everymeal.server.global.util.swagger;


import everymeal.server.global.exception.ApplicationException;

public interface BaseExceptionList {

    public ApplicationException getErrorReason();

    String getExplainError() throws NoSuchFieldException;
}
