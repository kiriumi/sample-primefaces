package webapi.rest;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import dto.Token;
import repository.TokenMapper;

@Named
@ApplicationScoped
public class RestUtils {

    @Inject
    private TokenMapper mapper;

    @Transactional
    public boolean hasToken(String token) {

        // DBにトークンがあるか、トークンの期限が切れていないかチェック
        Token tokenBean = mapper.selectByPrimaryKey(token);
        if (tokenBean == null) {
            return false;
        }

        // 設定ファイルから有効期限を取得
        ResourceBundle bundle = ResourceBundle.getBundle("WebApiConfig");
        String strExpiredMinutes = bundle.getString("token.expired.minutes");
        long expiredMinutes = Long.parseLong(strExpiredMinutes);

        LocalDateTime expiredDatetime = tokenBean.getUpdatedtime().plusMinutes(expiredMinutes);
        if (LocalDateTime.now().isAfter(expiredDatetime)) {
            return false;
        }

        // トークンが存在する場合は、トークンの更新日時を更新する
        tokenBean.setToken(token);
        tokenBean.setUpdatedtime(LocalDateTime.now());
        mapper.updateByPrimaryKeySelective(tokenBean);

        return true;
    }
}
