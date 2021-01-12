package com.atguigu.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfigurationTest extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //注入数据源
    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//      //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有权限访问跳转到的自定义403页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        
        //退出
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();

        http.formLogin()                            //自定义编写登录页面
                .loginPage("/login.html")            //登录页面
                .loginProcessingUrl("/user/login")  //登录访问路径
                .defaultSuccessUrl("/success.html")        //登陆成功后跳转到的页面
                .permitAll()
                .and().authorizeRequests()
                .antMatchers("/").permitAll()  //设置那些路径可以直接访问，不需要认证
                //当前登录用户，只有具有admin才可以访问这个路径
                //.antMatchers("/index").hasAuthority("admin,manager")
                //.antMatchers("/index").hasAnyAuthority("admin","manager")
                //hasRole
                .antMatchers("/index").hasAnyRole("sale")
                .anyRequest().authenticated()
                //记住我
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)   //设置有效时常，秒为单位
                .userDetailsService(userDetailsService)
                .and().csrf().disable();            //关闭csrf的保护
    }
}
