package felix.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry
                .addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns(
                        "/authentication/logout",
                        "/authentication/test/"
                        );
    }
}