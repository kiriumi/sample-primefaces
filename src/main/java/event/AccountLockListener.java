package event;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import dto.UserInfoExample;
import dto.UserInfoExample.Criteria;
import exception.AccountLokedException;
import repository.UserInfoMapper;
import security.LoginManager;

public class AccountLockListener implements PhaseListener {

    @Inject
    private LoginManager loginManager;

    @Inject
    private UserInfoMapper mapper;

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {

        if (StringUtils.isBlank(LoginManager.getUserId()) || !loginManager.isLogined()) {
            return;
        }

        UserInfoExample example = new UserInfoExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(LoginManager.getUserId()).andLockedEqualTo(true);

        long longLocked = mapper.countByExample(example);
        if (longLocked != 0) {
            loginManager.logout();
            throw new AccountLokedException();
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
