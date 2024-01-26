package everymeal.server.global.config;


import everymeal.server.global.exception.ExceptionList;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "jwt-user-auth",
        description = "JWT auth bearer token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info =
                new Info()
                        .title("EveryMeal API Doc")
                        .version("v0.0.1")
                        .description(
                                """
                            EveryMeal API 명세서입니다.<br>
                            스웨거 한계로 인해 Response에 공통 response의 data에 대한 정의가 되어있습니다. <br>
                            <details>
                                <summary> ERROR LIST </summary>
                                <table>
                                    <thead>
                                    <tr>
                                        <th>ERROR CODE</th>
                                        <th>STATUS</th>
                                        <th>ERROR MESSAGE</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        """
                                        + getErrorList()
                                        + """
                                    </tbody>
                                <table>
                            </details>
                            """);
        return new OpenAPI().components(new Components()).info(info);
    }

    //    @Bean
    //    public OperationCustomizer customize(){
    //        return (Operation operation, HandlerMethod handlerMethod) -> {
    //            ApiErrorCodeExample apiErrorCodeExample =
    //                handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
    //            // ApiErrorCodeExample 어노테이션 단 메소드 적용
    //            if (apiErrorCodeExample != null) {
    //                generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
    //            }
    //            return operation;
    //        };
    //    }
    //
    //    private void generateErrorCodeResponseExample(
    //        Operation operation, Class<? extends BaseExceptionList> type) {
    //        ApiResponses responses = operation.getResponses();
    //        // 해당 이넘에 선언된 에러코드들의 목록을 가져옵니다.
    //        BaseExceptionList[] errorCodes = type.getEnumConstants();
    //        // 400, 401, 404 등 에러코드의 상태코드들로 리스트로 모읍니다.
    //        // 400 같은 상태코드에 여러 에러코드들이 있을 수 있습니다.
    //        Map<String, List<ExampleHolder>> statusWithExampleHolders =
    //            Arrays.stream(errorCodes)
    //                .map(
    //                    baseErrorCode -> {
    //                        try {
    //                            ApplicationException errorReason = baseErrorCode.getErrorReason();
    //                            return ExampleHolder.builder()
    //                                .holder(
    //                                    getSwaggerExample(
    //                                        baseErrorCode.getExplainError(),
    //                                        errorReason))
    //                                .code(errorReason.getErrorCode())
    //                                .name(errorReason.getMessage())
    //                                .build();
    //                        } catch (NoSuchFieldException e) {
    //                            throw new RuntimeException(e);
    //                        }
    //                    })
    //                .collect(groupingBy(ExampleHolder::getCode));
    //        // response 객체들을 responses 에 넣습니다.
    //        addExamplesToResponses(responses, statusWithExampleHolders);
    //    }
    //
    //    private Example getSwaggerExample(String value, ApplicationException errorReason) {
    //        //ErrorResponse 는 클라이언트한 실제 응답하는 공통 에러 응답 객체입니다.
    //        Example example = new Example();
    //        example.description(value);
    //        example.setValue(ApplicationErrorResponse.error(errorReason));
    //        return example;
    //    }
    //
    //    private void addExamplesToResponses(
    //        ApiResponses responses, Map<String, List<ExampleHolder>> statusWithExampleHolders) {
    //        statusWithExampleHolders.forEach(
    //            (status, v) -> {
    //                Content content = new Content();
    //                MediaType mediaType = new MediaType();
    //                ApiResponse apiResponse = new ApiResponse();
    //                v.forEach(
    //                    exampleHolder -> {
    //                        mediaType.addExamples(
    //                            exampleHolder.getName(), exampleHolder.getHolder());
    //                    });
    //                content.addMediaType("application/json", mediaType);
    //                apiResponse.setContent(content);
    //                responses.addApiResponse(status, apiResponse);
    //            });
    //    }

    private String getErrorList() {
        StringBuilder errorList = new StringBuilder();
        ExceptionList[] exceptionLists = ExceptionList.values();
        for (ExceptionList exceptionList : exceptionLists) {
            errorList.append("<tr>");
            errorList.append("<td>").append(exceptionList.getCODE()).append("</td>");
            errorList.append("<td>").append(exceptionList.getHttpStatus()).append("</td>");
            errorList.append("<td>").append(exceptionList.getMESSAGE()).append("</td>");
            errorList.append("</tr>");
        }
        return errorList.toString();
    }
}
