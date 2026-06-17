package fr.but3.simondesdevises.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/activer", "/voir").hasRole("admin")
						.requestMatchers("/voter").hasRole("user")
						.anyRequest().authenticated())
				.formLogin(login -> login.defaultSuccessUrl("/voter", true))
				.logout(logout -> logout.logoutSuccessUrl("/login?logout"))
				.build();
	}

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails admin = User.withUsername("admin")
				.password(passwordEncoder.encode("admin"))
				.roles("admin")
				.build();
		UserDetails user1 = User.withUsername("user1")
				.password(passwordEncoder.encode("user1"))
				.roles("user")
				.build();
		UserDetails user2 = User.withUsername("user2")
				.password(passwordEncoder.encode("user2"))
				.roles("user")
				.build();
		UserDetails user3 = User.withUsername("user3")
				.password(passwordEncoder.encode("user3"))
				.roles("user")
				.build();
		return new InMemoryUserDetailsManager(admin, user1, user2, user3);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
