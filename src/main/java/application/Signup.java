package application;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.bind.JsonbBuilder;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.text.StringEscapeUtils;
import org.mybatis.cdi.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.common.base.Objects;

import domain.ReCatpchaResponse;
import domain.TwoStepVerificatior;
import dto.UserInfo;
import dto.UserInfoExample;
import dto.UserInfoExample.Criteria;
import exception.WebApplicationException;
import faces.BaseBackingBean;
import lombok.Getter;
import lombok.Setter;
import repository.UserInfoMapper;
import security.LoginManager;
import validation.constraints.AvailableChar;
import validation.constraints.Password;

@Named
@ViewScoped
public class Signup extends BaseBackingBean {

    private BCryptPasswordEncoder passwordEncoder;

    @AvailableChar
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
    private UserInfoMapper mapper;

    @Inject
    private LoginManager loginManager;

    @Inject
    private TwoStepVerificatior twoStep;

    private UserInfo user;

    @Getter
    @Setter
    private boolean isBot = true;

    public String init() {

        if (loginManager.isLogined()) {
            return redirect("/application/top");
        }

        this.user = getUser();
        if (user == null || !twoStep.isSuccessed()) {
            return redirect("login");
        }

        return null;
    }

    public void validate(ActionEvent event) {

        if (isBot) {

            ReCatpchaResponse reCatpchaReses = getReCatpchaResponse();

            if (!reCatpchaReses.isSuccess()) {
                messageService().addGlobalMessageWarn("??????????????????????????????????????????");
                getFacesContext().validationFailed();
                return;
            }

            // ????????????????????????????????????????????????
            if (reCatpchaReses.getScore().compareTo(BigDecimal.valueOf(0.6)) <= 0
                    || !Objects.equal(reCatpchaReses.getHostname(), request().getServerName())) {

                messageService().addGlobalMessageError("BOT????????????????????????");
                return;
            }
        }

        this.isBot = false;

        if (Objects.equal(id, password)) {
            messageService().addMessageError(List.of("id", "password"), "ID?????????????????????????????????????????????");
            return;
        }

        UserInfoExample example = new UserInfoExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        long count = mapper.countByExample(example);

        if (count != 0) {
            messageService().addMessageError("id", "??????ID???????????????????????????");
            return;
        }

    }

    //    @Inject
    //    UserTransaction tx;

    @Transactional
    public String signup() throws NotSupportedException, SystemException {

        //        tx.begin();

        if (passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }

        try {
            user.setId(id);
            user.setPassword(passwordEncoder.encode(password));
            mapper.insertSelective(user);

            // ??????????????????????????????????????????????????????????????????????????????????????????

            messageService().addGlobalMessageInfo("?????????????????????????????????");
            flash().setKeepMessages(true);
            //            tx.commit();

        } catch (Exception e) {
            //            tx.rollback();
            throw new WebApplicationException();

        }

        return redirect("login");
    }

    @Getter
    @Setter
    private String reCatpchaToken;

    private ReCatpchaResponse getReCatpchaResponse() {

        // reCATPCHA??????????????????????????????
        UriBuilder uri = UriBuilder.fromUri("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", "6Ldy9w8cAAAAAOvp_jALS5IrsFJK0xCGa8tUiwED")
                .queryParam("response", reCatpchaToken);

        Client client = ClientBuilder.newClient();
        String json = client.target(uri).request(MediaType.TEXT_PLAIN).get(String.class);
        ReCatpchaResponse response = JsonbBuilder.create().fromJson(StringEscapeUtils.unescapeJson(json),
                ReCatpchaResponse.class);

        return response;

    }

}
