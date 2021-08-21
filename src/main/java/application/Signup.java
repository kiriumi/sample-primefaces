package application;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.JsonbBuilder;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.common.base.Objects;

import domain.ReCatpchaResponse;
import domain.TwoStepVerificatior;
import domain.UserInfo;
import exception.WebApplicationException;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import security.LoginManager;
import validation.constraints.AvirableChar;
import validation.constraints.Password;

@Named
@ViewScoped
public class Signup extends BaseBackingBean {

    private BCryptPasswordEncoder passwordEncoder;

    @AvirableChar
    @NotBlank
    @Getter
    @Setter
    private String id;

    @Password
    @NotBlank
    @Getter
    @Setter
    private String password;

    @Inject
    private UserInfo user;

    @Inject
    private LoginManager loginManager;

    @Inject
    private TwoStepVerificatior twoStep;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }

        if (!twoStep.isSuccessed()) {
            return redirect("login");
        }
        return null;
    }

    public void validate(ActionEvent event) {

        if (isBot()) {
            throw new WebApplicationException("BOTです");
        }
        return;
    }

    public String signup() {

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));

        messageService().addMessage(FacesMessage.SEVERITY_INFO, "ユーザ登録が完了したよ");
        flash().setKeepMessages(true);

        return redirect("login");
    }

    @Getter
    @Setter
    private String reCatpchaToken;

    private Boolean isBot() {

        // reCATPCHAに検証をリクエストし
        UriBuilder uri = UriBuilder.fromUri("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", "6Ldy9w8cAAAAAOvp_jALS5IrsFJK0xCGa8tUiwED")
                .queryParam("response", reCatpchaToken);

        Client client = ClientBuilder.newClient();
        String response = client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        ReCatpchaResponse json = JsonbBuilder.create().fromJson(StringEscapeUtils.unescapeJson(response),
                ReCatpchaResponse.class);

        // レスポンスから検証結果を調査する
        if ((json.isSuccess() && json.getScore().compareTo(BigDecimal.valueOf(0.6)) > 0
                && Objects.equal(json.getHostname(), request().getServerName()))) {
            return false;
        }
        return true;
    }

}
