package samples.spring.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import samples.spring.jpa.domain.Person;
import samples.spring.jpa.repository.PersonRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * Created by izeye on 14. 12. 23..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaTest.JpaConfiguration.class)
public class JpaTest {

	@Autowired
	PersonRepository personRepository;

	@Test
	@Transactional
	public void test() {
		personRepository.save(new Person("Johnny", 20));

		List<Person> persons = personRepository.findAll();
		System.out.println(persons);
		System.out.println(persons.size());
	}

	@Configuration
	@EnableJpaRepositories(basePackages = "samples.spring.jpa.repository")
	public static class JpaConfiguration {
		@Bean
		public DataSource dataSource() {
			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			dataSource.setDriverClass(org.h2.Driver.class);
			dataSource.setUsername("sa");
			dataSource.setUrl("jdbc:h2:mem");
			dataSource.setPassword("");
			return dataSource;
		}

		@Bean
		public JpaVendorAdapter jpaVendorAdapter() {
			HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
			jpaVendorAdapter.setShowSql(true);
			jpaVendorAdapter.setGenerateDdl(true);
			jpaVendorAdapter.setDatabase(Database.H2);
			return jpaVendorAdapter;
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(
				DataSource dataSource,
				JpaVendorAdapter jpaVendorAdapter) {
			LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
					= new LocalContainerEntityManagerFactoryBean();
			entityManagerFactoryBean.setDataSource(dataSource);
			entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
			entityManagerFactoryBean.setPackagesToScan("samples.spring.jpa.domain");

			Properties jpaProperties = new Properties();
			jpaProperties.setProperty(
					"hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
			entityManagerFactoryBean.setJpaProperties(jpaProperties);

			return entityManagerFactoryBean;
		}

		@Bean
		public PlatformTransactionManager transactionManager() {
			return new JpaTransactionManager();
		}
	}

}
