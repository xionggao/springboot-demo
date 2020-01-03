package com.sb.demo.pub.config;

import com.sb.demo.pub.exception.AuthExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.concurrent.TimeUnit;

/**
 * OAuth2服务器配置
 *
 * @author xg
 */
@Configuration
public class OAuth2Config {

    //访问客户端密钥
    public static final String CLIENT_SECRET = "123456";
    //访问客户端ID
    public static final String CLIENT_ID = "client_1";
    //鉴权模式
    public static final String[] GRANT_TYPE = {"authorization_code", "password", "refresh_token"};

    /**
     * 资源服务器
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Autowired
        private AuthExceptionHandler exceptionHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.stateless(false).accessDeniedHandler(exceptionHandler).authenticationEntryPoint(exceptionHandler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // 登录请求和OPTIONS类型请求不需要鉴权
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().authorizeRequests()
                    .antMatchers("/oauth/*", "/user/login").permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated();
            super.configure(http);
        }
    }

    /**
     * 认证授权服务器
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private RedisConnectionFactory connectionFactory;

        @Bean
        public RedisTokenStore redisTokenStore() {
            return new RedisTokenStore(connectionFactory);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            // 允许表单认证
            oauthServer.allowFormAuthenticationForClients().tokenKeyAccess("isAuthenticated()").checkTokenAccess("permitAll()");
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            final String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode(CLIENT_SECRET);
            // 配置客户端
            clients.inMemory().withClient(CLIENT_ID).authorizedGrantTypes(GRANT_TYPE)
                    .scopes("all").secret(finalSecret);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            // Token存放到redis
            endpoints.tokenStore(redisTokenStore()).authenticationManager(authenticationManager);
            // 配置TokenService参数
            DefaultTokenServices tokenService = new DefaultTokenServices();
            tokenService.setTokenStore(endpoints.getTokenStore());
            tokenService.setSupportRefreshToken(true);
            tokenService.setClientDetailsService(endpoints.getClientDetailsService());
            tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
            tokenService.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1));
            tokenService.setRefreshTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1));
            tokenService.setReuseRefreshToken(false);
            endpoints.tokenServices(tokenService);
        }
    }
}