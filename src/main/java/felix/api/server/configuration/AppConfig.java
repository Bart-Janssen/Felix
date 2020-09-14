package felix.api.server.configuration;

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
                        "/authentication/2fa/*",
                        "/chats/*",
                        "/items/*",
                        "/items/inventory/*",
//                        "/player/*",
                        "/player/friends/*",
                        "/player/friends/invite/*",
                        "/player/friends/invites/outgoing/*",
                        "/player/friends/invites/incoming/*"
                        );
    }
}