package academy.mindswap.springSecurity;

import academy.mindswap.springSecurity.command.CreateUserDto;
import academy.mindswap.springSecurity.command.UserDto;
import academy.mindswap.springSecurity.models.Role;
import academy.mindswap.springSecurity.models.User;
import academy.mindswap.springSecurity.services.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	/* @PostConstruct
	public void init(){
		String url = "https://meowfacts.herokuapp.com";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + "acessToken");

		HttpEntity<String> entity = new HttpEntity<String>("hi", headers);

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.postForObject(url, entity, String.class);
		//ResponseEntity<String> response= restTemplate.getForEntity(url, String.class);
	}*/

}
