package com.scor.security;


import com.scor.security.dev.DummyAuthenticationProvider;
import com.scor.security.filter.RedirectionTokenFilter;
import com.scor.security.ldap.CustomFilterBasedLdapUserSearch;
import com.scor.security.ldap.CustomLdapAuthoritiesPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.GlobalSunJaasKerberosConfig;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

@EnableWebSecurity
@PropertySource("classpath:application.properties")
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ldap.domain}")
	private String ldapDomain;
	@Value("${ldap.url}")
	private String ldapUrl;
	@Value("${keytab.location}")
	private Resource keytabLocation;
	// private Resource keytabLocation = new
	// ClassPathResource("securityFiles/SVC_DCVDEVARC.keytab");
	@Value("${krbConfLocation.path}")
	private String krbConfLocation;
	// private String krbConfLocation =
	// "src/main/resources/securityFiles/krb5.conf";
	@Value("${krbConfLocation.debug}")
	private boolean krbConfDebug;
	@Value("${service.principal}")
	private String servicePrincipal;
	@Value("${service.principal.debug}")
	private boolean servicePrincipalDebug;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().cors();
		http.csrf().ignoringAntMatchers("/users/kerb").disable().cors().and().authorizeRequests().antMatchers(HttpMethod.GET, "/users/kerb").permitAll()
				.antMatchers(HttpMethod.POST, "/run/update").permitAll().and().authorizeRequests().antMatchers("/**").authenticated().and().httpBasic()
				.authenticationEntryPoint(restSpenegoEntryPoint()).and()
				.addFilterBefore(spnegoAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(redirectionTokenFilter(), ExceptionTranslationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response
		// must not be the wildcard '*' when the request's credentials mode is
		// 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		// configuration.setAllowedHeaders(Arrays.asList("Content-Type",
		// "X-Requested-With", "accept", "Origin",
		// "Access-Control-Request-Method", "Access-Control-Request-Headers"));
		configuration
				.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
				.authenticationProvider(kerberosServiceAuthenticationProvider());
	}

	/*********************
	 * DEV DEPENDENCIES
	 *************************************/

	@Bean
	DummyAuthenticationProvider customAuthenticationProvider() {
		return new DummyAuthenticationProvider();
	}

	/*********************
	 * BASIC FILTER AND REST ENTRY POINT
	 *********************/

	// @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	// @Override
	// public AuthenticationManager authenticationManagerBean() throws Exception
	// {
	// return super.authenticationManagerBean();
	// }

	@Bean
	public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter() throws Exception {
		SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter = new SpnegoAuthenticationProcessingFilter();
		// spnegoAuthenticationProcessingFilter.setSuccessHandler(customAuthenticationSuccessHandler());
		spnegoAuthenticationProcessingFilter.setAuthenticationManager(authenticationManagerBean());
		return spnegoAuthenticationProcessingFilter;
	}

	@Bean
	RedirectionTokenFilter redirectionTokenFilter() {
		return new RedirectionTokenFilter();
	}

	// @Bean
	// PolkaLifeFilter polkaLifeFilter() {
	// return new PolkaLifeFilter();
	// }

	// @Bean
	// DirectAuthenticationFilter directAuthenticationFilter() {
	// return new DirectAuthenticationFilter();
	// }

	@Bean
	public RestSpenegoEntryPoint restSpenegoEntryPoint() {
		return new RestSpenegoEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandlerImpl customAccessDeniedHandlerImpl() {
		return new CustomAccessDeniedHandlerImpl();
	}

	@Bean
	public DelegatingAccessDeniedHandler delegatingAccessDeniedHandler() {
		LinkedHashMap<Class<? extends AccessDeniedException>, AccessDeniedHandler> handlers = new LinkedHashMap<>();
		handlers.put(com.scor.security.exception.ConnectionAccessDeniedException.class,
				customAccessDeniedHandlerImpl());
		return new DelegatingAccessDeniedHandler(handlers, customAccessDeniedHandlerImpl());

	}

	/************************ AUTHENTICATION PROVIDERS ***********************/

	@Bean
	ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		return new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
	}

	@Bean
	KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() throws Exception {
		KerberosServiceAuthenticationProvider ksap = new KerberosServiceAuthenticationProvider();
		ksap.setTicketValidator(sunJaasKerberosTicketValidator());
		ksap.setUserDetailsService(ldapUserDetailsService());
		return ksap;
	}

	/********************* KERBEROS AHTUENTICATION DEPENDENCIES ********/

	@Bean
	GlobalSunJaasKerberosConfig globalSunJaasKerberosConfig() {
		GlobalSunJaasKerberosConfig globalSunJaasKerberosConfig = new GlobalSunJaasKerberosConfig();
		globalSunJaasKerberosConfig.setDebug(krbConfDebug);
		globalSunJaasKerberosConfig.setKrbConfLocation(krbConfLocation);
		return globalSunJaasKerberosConfig;
	 }

	@Bean
	SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() throws Exception {
//		if (krbConfDebug) {
//			System.out.println("Set value for sun.security.krb5.debug = true");
//			System.setProperty("sun.security.krb5.debug", Boolean.toString(this.krbConfDebug));
//		}
//		if (StringUtils.isNotBlank(krbConfLocation)) {
//			System.out.println("Set value for sun.security.krb5.conf = " + this.krbConfLocation);
//			System.setProperty("sun.security.krb5.conf", this.krbConfLocation);
//		}
////		
//		GlobalSunJaasKerberosConfig krb5Config = new GlobalSunJaasKerberosConfig();
//		krb5Config.setKrbConfLocation(this.krbConfLocation);
//		krb5Config.setDebug(krbConfDebug);
//		krb5Config.afterPropertiesSet();

		SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
		ticketValidator.setKeyTabLocation(keytabLocation);
		ticketValidator.setServicePrincipal(servicePrincipal);
		ticketValidator.setHoldOnToGSSContext(false);
		ticketValidator.setDebug(servicePrincipalDebug);
		return ticketValidator;
	}

	@Bean
	LdapUserDetailsService ldapUserDetailsService() {
		return new LdapUserDetailsService(filterBasedLdapUserSearch(), ldapAuthoritiesPopulator());
	}

	@Bean
	CustomFilterBasedLdapUserSearch filterBasedLdapUserSearch() {
		return new CustomFilterBasedLdapUserSearch("DC=eu,DC=scor,DC=local", "sAMAccountName={0}",
				kerberosLdapContextSource());
	}

	@Bean
	SunJaasKrb5LoginConfig loginConfig() {
		SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
		loginConfig.setKeyTabLocation(keytabLocation);
		loginConfig.setServicePrincipal(servicePrincipal);
		loginConfig.setDebug(servicePrincipalDebug);
		loginConfig.setIsInitiator(true);
		return loginConfig;

	}

	@Bean
	KerberosLdapContextSource kerberosLdapContextSource() {
		KerberosLdapContextSource kerberosLdapContextSource = new KerberosLdapContextSource(ldapUrl);
		kerberosLdapContextSource.setLoginConfig(loginConfig());
		return kerberosLdapContextSource;
	}

	@Bean
	CustomLdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		CustomLdapAuthoritiesPopulator ldapAuthoritiesPopulator = new CustomLdapAuthoritiesPopulator(
				kerberosLdapContextSource(), "CN=Users,DC=eu,DC=scor,DC=local");
		ldapAuthoritiesPopulator.setGroupRoleAttribute("ou");
		ldapAuthoritiesPopulator.setSearchSubtree(false);
		ldapAuthoritiesPopulator.setRolePrefix("ROLE_");
		ldapAuthoritiesPopulator.setConvertToUpperCase(true);
		return ldapAuthoritiesPopulator;
	}
}
