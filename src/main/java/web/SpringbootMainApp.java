package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import web.model.User;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class SpringbootMainApp extends SpringBootServletInitializer {

    private static RestTemplate restTemplate;
    private static HttpHeaders httpHeaders;

    @Autowired
    public SpringbootMainApp(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMainApp.class, args);
        String cookie1 = getAllUser().toString();
        String cookie2 = cookie1.substring(cookie1.indexOf("Set-Cookie") + 12);
        String cookie = cookie2.substring(0, cookie2.indexOf(" ") - 1);
        httpHeaders.set("Cookie", cookie);

        System.out.println("Итоговый код: " + new StringBuilder()
                .append(addUser(new User(3L, "James", "Brown", (byte) 25)).getBody())
                .append(editUser(new User(3L, "Thomas", "Shelby", (byte) 25)).getBody() +
                        deleteUser(3L).getBody()));
    }

    //Получение всех пользователей - …/api/users ( GET )
    public static ResponseEntity<List<User>> getAllUser() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(
                "http://91.241.64.178:7081/api/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                });
        return responseEntity;
    }

    //Добавление пользователя - …/api/users ( POST )
    public static ResponseEntity<String> addUser(User user) {
        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        return restTemplate.postForEntity("http://91.241.64.178:7081/api/users", httpEntity, String.class);
    }

    //Изменение пользователя - …/api/users ( PUT )
    public static ResponseEntity<String> editUser(User user) {
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        return restTemplate.exchange("http://91.241.64.178:7081//api/users", HttpMethod.PUT, httpEntity, String.class, user.getId());
    }

    //Удаление пользователя - …/api/users /{id} ( DELETE )
    public static ResponseEntity<String> deleteUser(long id) {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        return restTemplate.exchange("http://91.241.64.178:7081//api/users/{id}", HttpMethod.DELETE, httpEntity, String.class, id);
    }
}
