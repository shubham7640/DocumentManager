package com.springReact.DocumentManager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // by default csrf is enabled on login page provided by spring security so we need to disable it for testing
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(
//                request -> request.requestMatchers("/user/test").permitAll()
                // We can use "/user/**" to ignore authentication for all endpoints starting with /user
                // Please not by default /login and /logout are open and do not need authentication
                        request -> request.requestMatchers("/user/**").permitAll()
                        .anyRequest().authenticated())
                .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        // DaoAuthenticationProvider is the default authentication Provider
//        // This is invoked via Authentication Manager named ProviderManager
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
////        Please note here we are using static user details set using userDetailsService bean
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        //The userDetailsService bean will be picked directly from constructor declaration below
//        return new ProviderManager(daoAuthenticationProvider);
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        MyOwnAuthenticationProvider myOwnAuthenticationProvider = new MyOwnAuthenticationProvider(userDetailsService);
        return new ProviderManager(myOwnAuthenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user1 = //User.withDefaultPasswordEncoder()
                User.withUsername("Kashyap") // Created user without password encoding
                .password("Krishna")
                .roles("USER")
                .build();
        var user2 = User.withDefaultPasswordEncoder() // Created user with password encoding
                .username("Radhe")
                .password("{noop}Krishna")//TODO : check how can we block password encoding
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(List.of(user1,user2));
    }

//    @Bean
//    public UserDetailsService inMemoryUserDetailsManager() {
//        return new InMemoryUserDetailsManager(
//            User.withUsername("Kashyap").password("Krishna").roles("USER").build(),
//            User.withUsername("Radhe").password("Krishna").roles("USER").build()
//        );
//    }
}
