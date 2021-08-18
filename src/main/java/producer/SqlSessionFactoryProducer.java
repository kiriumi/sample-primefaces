package producer;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

@ApplicationScoped
public class SqlSessionFactoryProducer {

	@Produces
	@ApplicationScoped
	@SessionFactoryProvider
	private SqlSessionFactory produceSqllSessionFactory() throws IOException {

		final InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
		final SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		return builder.build(inputStream);
	}

}
