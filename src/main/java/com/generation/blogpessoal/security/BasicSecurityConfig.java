package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Classe BasicSecurityConfig
 * 
 * Esta classe é responsável por habilitar a segurança básica da aplicação e o login na aplicação.
 * 
 * Para habilitar a segurança HTTP no Spring, precisamos extender (herdar) a Classe WebSecurityConfigurerAdapter
 * para fornecer uma configuração padrão no método configure (HttpSecurity http)
 * 
 * A configuração padrão garante que qualquer requisição enviada para a API seja autenticada com login
 * baseado em formulário ou autenticação via Browser.
 * 
 * Para personaliazar a autenticação utilizaremos a sobrecarga dos métodos da Classe WebSecurityConfigurerAdapter
 */

/*
 * A anotação @EnableWebSecurity habilita a configuração de segurança padrão do Spring Security na nossa API
 */

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * A anotação @Autowired insere uma injeção de Dependência 
	 * 
	 * Como iremos utilizar os usuários salvos no nosso Banco de dados,
	 * na table tb_usuarios, para efetuar o login na api precisamos injetar um objeto da
	 * Interface UserDetailService que será implementada na Classe UserDetailServiceImpl que fará o acesso 
	 * ao nosso Banco de dados para recuperar os dados do usuário.
	 */
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*
	 * Sobrescreve (@Override) o primeiro método Configure, que tem a função de criar uma nova instância da
	 * Classe AuthenticationManagerBuilder e define que o login será efetuado através dos usuários criados no
	 * Banco de dados. Para recuperar os dados do usuário no Banco de Dados utilizaremos a Interface
	 * UserDetailsService.
	 * Outra alternativa de login seria a criação de um usuárop em memória, que verei mais para frente.
	 * 
	 * O método é do tipo protected por definição da classe. 
	 *
	 */
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		/*
		 * O objeto auth registra uma nova instância do objeto userDetailsService da interface
		 * UserDetailsService implementada na Classe UserDetailsServiceImpl para recuperar os dados dos 
		 * usuários gravados no Banco de dados. 
		 */
		
		auth.userDetailsService(userDetailsService);
		
		auth.inMemoryAuthentication()
			.withUser("root")
			.password(passwordEncoder().encode("root"))
			.authorities("ROLE_USER");
		
	}
	
	/*
	 * A anotação @Bean transforma a instância retornada pelo método como um objeto gerenciado pelo Spring, 
	 * desta forma, ele pode ser injetado em qualquer classe, a qualquer momento que você precisar sem a 
	 * necessidade de usar a anotação @Autowired
	 * 
	 * O método abaixo é responsável por criptografar a senha do usuário utilizando o método has Bcrypt.
	 */

	@Bean
	private PasswordEncoder passwordEncoder() {
		
		return new  BCryptPasswordEncoder();
	}
	
	/*
	 * Sobrescreve (@Override) o segundo método Configure que é responsável por criar uma instância da Classe
	 * HttpSecurity, que permite configurar a segurança baseada na web para solicitações http específicas 
	 * (endpoints)
	 */
	
	@Override
	private void configure(HttpSecurity http) throws Exception{
		
		/*
		 * antMatchers().permitAll -> Endpoint liberado de autenticação
		 * 
		 * HttpMethod.OPTIONS -> O parâmetro HttpMethod.OPTIONS permite que o cliente(frontend), possa 
		 * descobrir quais são as opções de requisição permitidas para um determinado recurso em um servidor.
		 * Nesta implementação, está sendo liberada todas as opções das requisições através do método 
		 * permitAll().
		 * 
		 * anyRequest().authenticated() -> Todos os demais endpoints serão autenticados
		 * 
		 * httpBasic -> Tipo de autenticação http (Basic Security)
		 * 
		 * Http Sessions: As sessões HTTP são um recurso que permite que os servidores da Web mantenham a 
		 * identidade do usuário e armazenem dados específicos do usuário durante várias interações 
		 * (request/response) entre um aplicativo cliente e um aplicativo da web.
		 * 
		 * sessionManagement() -> Cria um gerenciador de Sessoes
		 * 
		 * sessionCreationPolicy (SessionCreationPolicy.STATELESS) -> Define como o Spring Security irá
		 * criar (ou não) as sessões.
		 * 
		 * STATELESS -> Nunca será criada uma sessão, ou seja,basta enviar o token através do cabeçalho 
		 * da requisição que a mesma será processada.
		 * 
		 * cors -> O compartilhamento de recursos de origem cruzada (CORS) surgiu porquê os navegadores não
		 * permitem solicitações feitas por um domínio (endereço) diferente daquele de onde o site foi 
		 * carregado. Desta forma o Frontend da aplicação, por exemplo, obrigatoriamente teria que estar 
		 * no mesmo domínio que o Backend. Habilitando o CORS, o Spring desabilita esta regra e permite conexões
		 * de outros domínios.
		 * 
		 * CSRF: O cross-site request forgery (falsificação de solicitação entre sites), é um tipo de ataque 
		 * no qual comandos não autorizados são transmitidos a partir de um usuário em que a aplicação web confia.
		 * 
		 * csrf().disable() -> Esta opção de proteção é habilitada por padrão no Spring Security, entretanto
		 * precisamos desabilitar, caso contrário, todos os endpoints que responem ao verbo POST não serão 
		 * executados.
		 */
		
		http.authorizeRequests()
			.antMatchers("/usuarios/logar").permitAll()
			.antMatchers("/usuarios/cadastrar").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.anyRequest().authenticated()
			.and().httpBasic()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().cors()
			.and().csrf().disable();
				
	}
}
