package felix.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        System.out.println("pre");
        if (request.getMethod().equals("OPTIONS")) return true;
        System.out.println("asd " + request.getHeader("Authorization"));
        if (new JwtTokenGenerator().decodeJWT(request.getHeader("Authorization")) == null)
        {
            log.info("Not allowed access");
            response.setStatus(403);
            return false;
        }
        return true;
    }
}