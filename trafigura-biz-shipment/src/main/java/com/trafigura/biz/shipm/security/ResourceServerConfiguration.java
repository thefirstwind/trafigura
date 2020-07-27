package com.trafigura.biz.shipm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
	
       http.requestMatchers().anyRequest()
        .and().authorizeRequests()
                .antMatchers("/").authenticated()
                .and().authorizeRequests().antMatchers("/user/**").authenticated()
                .and().authorizeRequests().antMatchers("/db/**").authenticated()
//                .and().authorizeRequests().antMatchers("/shipment/**").authenticated()
                .and().authorizeRequests().antMatchers("/h2/**").permitAll()
                .and().authorizeRequests().antMatchers("/**").authenticated()
                ; 
    	http.csrf().disable();
    	http.headers().frameOptions().sameOrigin();
    	http.headers().frameOptions().disable();
    }
}
