package domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;

import lombok.Getter;

@ApplicationScoped
public class NgPasswords implements Serializable {

    @Getter
    private List<String> list;

    @PostConstruct
    public void loadNgPasswords() throws IOException {

        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("ng-passwords.dat");

        this.list = IOUtils.readLines(in, "UTF-8");
    }
}
