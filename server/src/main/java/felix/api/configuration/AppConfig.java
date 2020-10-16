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
                        "/authentication/logout/",
                        "/authentication/test/",
                        "/authentication/2fa/enable/",
                        "/authentication/2fa/disable/",
                        "/friends/",
                        "/friends/*",
                        "/friends/invites/",
                        "/friends/invites/outgoing/",
                        "/friends/invites/outgoing/*",
                        "/friends/invites/incoming/",
                        "/friends/invites/incoming/*",
                        "/friends/invites/incoming/accept/",
                        "/friends/invites/incoming/accept/*",
                        "/friends/invites/incoming/decline/",
                        "/friends/invites/incoming/decline/*",
                        "/friends/invites/outgoing/cancel/",
                        "/friends/invites/outgoing/cancel/*"
                        );
    }
}