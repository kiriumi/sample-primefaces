package security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import exception.AccountLokedException;

public class LoginCheckLisener implements PhaseListener {

	@Inject
	private FacesContext facesCtx;

	@Inject
	private ExternalContext extCtx;

	@Inject
	private LoginManager loginManager;

	@Override
	public void afterPhase(final PhaseEvent event) {
	}

	@Override
	public void beforePhase(final PhaseEvent event) {

		ResourceBundle bundle = ResourceBundle.getBundle("ApplicationConfig");
		List<String> securityConstraintUrls = Arrays.asList(bundle.getString("security.constraint.url").split(","));

		if (!isSecuredPage(securityConstraintUrls) && !loginManager.isLogined()) {

			// 非認証画面の場合
			if (StringUtils.isNotBlank(extCtx.getSessionId(false)) && !facesCtx.isPostback()) {
				// セッションハイジャック防止のため毎回セッションIDを変更
				HttpServletRequest req = (HttpServletRequest) extCtx.getRequest();
				req.changeSessionId();
			}
			return;
		}

		if (loginManager.isLocked()) {
			throw new AccountLokedException();
		}

		if (extCtx.getSession(false) == null || !loginManager.isLogined()) {
			// 未認証の場合、ログイン画面にリダイレクト
			String loginPage = bundle.getString("login.page");
			loginPage = loginPage.startsWith("/") ? loginPage : StringUtils.join("/", loginPage);
			String loginPagePath = String.join("", extCtx.getRequestContextPath(), loginPage);

			try {
				extCtx.redirect(loginPagePath);
			} catch (IOException e) {
			}
		}

	}

	private boolean isSecuredPage(List<String> urls) {

		for (String url : urls) {

			url = url.trim();
			url = url.startsWith("/") ? url : StringUtils.join("/", url);

			if (extCtx.getRequestServletPath().startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
