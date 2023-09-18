package everymeal.server.global.util.authresolver;


import everymeal.server.global.util.JwtUtil;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class UserJwtResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class) && AuthenticatedUser.class.equals(parameter.getParameterType());
    }

    @Nullable
    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception
    {
        Auth auth = parameter.getMethodAnnotation(Auth.class);
        if (auth == null) {
            throw new Exception("토큰을 통해 userId를 추출하는 메서드에는 @Auth 어노테이션을 붙여주세요.");
        }
        String authorization = webRequest.getHeader("Authorization");
        if (!auth.require() && authorization == null){
            return null;
        }else{
            return jwtUtil.getAuthenticateUserFromAccessToken(authorization);
        }
    }
}
