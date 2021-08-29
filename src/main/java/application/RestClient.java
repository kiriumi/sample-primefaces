package application;

import java.util.concurrent.TimeUnit;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dto.UserInfo;
import faces.BaseBackingBean;

@Named
@ViewScoped
public class RestClient extends BaseBackingBean {

    private static final UriBuilder BASE_URI = UriBuilder
            .fromUri("http://localhost:8080/sample-primefaces/rest/service");

    private static final Client CLIENT = ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.MILLISECONDS)
            .readTimeout(10, TimeUnit.MILLISECONDS).build();

    public void getToken() {

        Response response = null;

        try {
            response = CLIENT.target(BASE_URI).path("token")
                    .request(MediaType.TEXT_PLAIN).header("Origin-Context", "http://localhost:8080/sample-primefaces")
                    .post(Entity.text("aaaaa"));

        } catch (ProcessingException e) {
            messageService().addGlobalMessageWarn("タイムアウトが発生したからもう一回やってみて");
            return;
        }

        if (response.getStatus() == HttpServletResponse.SC_OK) {
            String token = response.readEntity(String.class);
            session().put("webapi.RestClient.token", token);

        } else {
            messageService().addGlobalMessageWarn("失敗したからもう一回やってみて");
        }

    }

    public void signup() {

        String token = (String) session().get("webapi.RestClient.token");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserInfo user = new UserInfo();
        user.setId("9999");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setEmail("z@z");

        Response response = null;

        try {
            response = CLIENT.target(BASE_URI).path("signup")
                    .request(MediaType.APPLICATION_JSON).header("Token", token)
                    .post(Entity.json(user));

        } catch (ProcessingException e) {
            messageService().addGlobalMessageWarn("タイムアウトが発生したからもう一回やってみて");
            return;
        }

        if (response.getStatus() == HttpServletResponse.SC_OK) {
            messageService().addGlobalMessageInfo("成功したよ");

        } else {
            messageService().addGlobalMessageWarn("失敗したからもう一回やってみて");
        }

    }

    public void deleteToken() {

        String token = (String) session().get("webapi.RestClient.token");

        Response response = null;

        try {
            response = CLIENT.target(BASE_URI).path("deleteToken")
                    .request(MediaType.TEXT_PLAIN).post(Entity.text(token));

        } catch (ProcessingException e) {
            messageService().addGlobalMessageWarn("タイムアウトが発生したからもう一回やってみて");
            return;
        }

        if (response.getStatus() == HttpServletResponse.SC_OK) {
            messageService().addGlobalMessageInfo("成功したよ");
            session().remove("webapi.RestClient.token");

        } else {
            messageService().addGlobalMessageWarn("失敗したからもう一回やってみて");
        }
    }

}
