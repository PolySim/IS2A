package fr.apgis4.tp509;

import javax.sql.DataSource;

import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
public class Security {

  @Bean
  public SecurityFilterChain mesautorisations(HttpSecurity http)
      throws Exception {
    return http
        .authorizeHttpRequests(authorize -> authorize
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
            .requestMatchers("/public", "/error").permitAll()
            .anyRequest().authenticated())
        // .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults())
        .logout(logout -> logout
            .logoutSuccessUrl("/public")
            .permitAll())
        .rememberMe(remember -> remember
            .key("cleUniqueEtSecretePourRememberMe")
            .tokenValiditySeconds(86400))
        .build();
  }

  @Bean
  // public UserDetailsService mesutilisateurs() {
  // UserDetails user1 = User.builder()
  // .username("root")
  // // .password(encoder().encode("root"))
  // .password("{bcrypt}$2y$10$1YpzGlk6kcRbpuVEuQwJTepiJBujEXpUTUlsCSehV4OewLIIDfeae")
  // .roles("ADMIN")
  // .build();
  // return new InMemoryUserDetailsManager(user1);
  public UserDetailsService mesutilisateurs(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  // Bean
  // public PasswordEncoder encoder() {
  // return new BCryptPasswordEncoder();
  // }
  @Bean
  public PasswordEncoder encoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
