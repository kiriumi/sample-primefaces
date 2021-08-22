package faces;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import util.FacesUtils;
import util.MessageUtils;

@RequestScoped
public class MessageService {

    @Inject
    private FacesContext facesContext;

    public void addGlobalMessageInfo(@NotBlank String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    public void addGlobalMessageInfo(@NotBlank String id, Object... params) {
        String message = MessageUtils.getMessage(id, params);
        addGlobalMessageInfo(message);
    }

    public void addGlobalMessageWarn(@NotBlank String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    public void addGlobalMessageWarn(@NotBlank String id, Object... params) {
        String message = MessageUtils.getMessage(id, params);
        addGlobalMessageWarn(message);
    }

    public void addGlobalMessageError(@NotBlank String message) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public void addGlobalMessageError(@NotBlank String id, Object... params) {
        String message = MessageUtils.getMessage(id, params);
        addGlobalMessageError(message);
    }

    public void addMessageError(@NotBlank String clientId, @NotBlank final String message) {

        String targetId = FacesUtils.findComponent(clientId).getClientId();
        facesContext.addMessage(targetId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));

        if (!facesContext.isValidationFailed()) {
            facesContext.validationFailed();
        }
    }

    public void addMessageError(@NotBlank String clientId, @NotBlank final String id, Object... params) {
        String message = MessageUtils.getMessage(id, params);
        addGlobalMessageError(message);
    }

    public void addMessageError(@NotEmpty List<String> ids, @NotBlank final String message) {
        ids.forEach(id -> {
            addMessageError(id, message);
        });
    }

    public void addMessageError(@NotEmpty List<String> clientIds, @NotBlank final String id, Object... params) {

        String message = MessageUtils.getMessage(id, params);
        clientIds.forEach(clientId -> {
            addMessageError(clientId, message);
        });
    }

}
