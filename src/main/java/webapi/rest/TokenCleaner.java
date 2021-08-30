package webapi.rest;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;

import dto.TokenExample;
import dto.TokenExample.Criteria;
import repository.TokenMapper;

@Singleton
@Startup
public class TokenCleaner {

    @Inject
    private TokenMapper mapper;

    @PostConstruct
    @Transactional
    public void cleanupToken() throws InterruptedException {

        ResourceBundle bundle = ResourceBundle.getBundle("WebApiConfig");

        String strExpiredMinutes = bundle.getString("token.cleanup.expired.minutes");
        long expiredMinutes = Long.parseLong(strExpiredMinutes);

        String strIntervalMinutes = bundle.getString("token.cleanup.interval.minutes");
        long interbvalMinutes = Long.parseLong(strIntervalMinutes);

        TokenExample example = new TokenExample();
        Criteria criteria = example.createCriteria();
        criteria.andUpdatedtimeLessThan(LocalDateTime.now().minusMinutes(expiredMinutes));

        Runnable cleaner = () -> {

            while (true) {
                mapper.deleteByExample(example);
                try {
                    Thread.sleep(interbvalMinutes * 1000 * 60);

                } catch (InterruptedException e) {
                }
            }
        };
        new Thread(cleaner).start();
    }
}
