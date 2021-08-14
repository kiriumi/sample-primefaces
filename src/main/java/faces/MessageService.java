package faces;

import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import lombok.extern.log4j.Log4j2;

@RequestScoped
@Log4j2
public class MessageService {

    @Inject
    private FacesContext facesContext;

    public void addMessage(final Severity severity, @NotBlank final String message) {
        facesContext.addMessage(null, new FacesMessage(severity, message, ""));
        log(severity, message);
    }

    public void addMessageById(final Severity severity, @NotBlank final String messageId, final Object... params) {
        addMessageByProperties("ApplicationMessages", severity, messageId, params);
    }

    public void addValidaionMessageById(final Severity severity, @NotBlank final String messageId,
            final Object... params) {
        addMessageByProperties("ValidationMessages", severity, messageId, params);
    }

    public boolean isEmpty() {

        if (facesContext.getMessageList().isEmpty()) {
            return true;
        }
        return false;
    }

    private void addMessageByProperties(final String baseName, final Severity severity, final String messageId,
            final Object... params) {

        String rawMessage = ResourceBundle.getBundle(baseName).getString(messageId);
        String message = String.format(rawMessage, params);
        facesContext.addMessage(null, new FacesMessage(severity, message, ""));

        log(severity, message);
    }

    private void log(final Severity severity, final String message) {

        if (severity.equals(FacesMessage.SEVERITY_INFO)) {
            log.info(message);
        } else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
            log.warn(message);
        } else {
            log.error(message);
        }
    }

}
