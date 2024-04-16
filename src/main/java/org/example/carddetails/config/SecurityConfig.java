package org.example.carddetails.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.POST,"/api/v1/customer/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/customer/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/customer/hello").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/customer/addCard").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customer/customers").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/customer/navbar").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customer/profile-details").hasAuthority("USER")

                        //Hotel Controller
                        .requestMatchers(HttpMethod.GET, "/api/v1/hotels/search/**").hasAuthority("USER")

                        //graphql
                        .requestMatchers("/graphql").permitAll()
                        .requestMatchers("/graphiql").permitAll()

                        //for swagger ui
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        //Booking Controller
                        .requestMatchers(HttpMethod.POST,"/api/v1/booking/bookingDetails").hasAuthority("USER")
                        //Payment Controller
                        .requestMatchers(HttpMethod.POST,"api/v1/payment/pay").hasAuthority("USER")
                        .anyRequest().authenticated());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/customer/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((
                                ((request, response, authentication) -> SecurityContextHolder.clearContext())
                                ))
                );
        return http.build();
    }
}
