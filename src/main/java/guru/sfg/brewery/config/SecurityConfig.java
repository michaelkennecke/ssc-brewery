package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher(("/api/**")));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use in production
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                            .mvcMatchers("/brewery/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                            .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries/**").hasAnyRole("ADMIN", "CUSTOMER");
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // if you not comment this out, you overrite the spring boot default implementation/ configuration
    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    //}

      /*@Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("spring")
                .password("123")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }*/

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("(123")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("$2a$10$veHwLLJgX9G2USBi.79Ao.YWmSZKrzKpnhpxIx1j9nk2kMWR.H5SK")
                .roles("USER");

        // you can add new users also in this way
        auth.inMemoryAuthentication()
                .withUser("scott")
                .password("{bcrypt15}$2a$15$neKM0jU3QMU4ex3MTPPilOCPgg36Z51K/w12yjizw1BFv6qXKcCLG") // noop specifies a passworder encoder, otherwise you would get an error
                .roles("CUSTOMER");
    }*/
}
