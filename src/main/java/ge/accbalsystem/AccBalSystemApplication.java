package ge.accbalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccBalSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccBalSystemApplication.class, args);
	}

	//ToDo Сделать планированное обновление курса валют со стор. API
}
