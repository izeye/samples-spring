package samples.spring.org.springframework.jdbc.core;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by izeye on 14. 11. 30..
 */
public class JdbcTemplateTest {

    @Test
    public void testJdbc_H2_Mem() {
        testJdbcTemplate("jdbc:h2:mem");
    }

    @Test
    @Ignore
    public void testJdbc_H2_Mem_() {
        testJdbcTemplate("jdbc:h2:mem:");
    }

    @Test
    public void testJdbc_H2_Mem_Test() {
        testJdbcTemplate("jdbc:h2:mem:test");
    }

    @Test
    public void testJdbc_H2_Mem_Test_DB_CLOSE_DELAY() {
        testJdbcTemplate("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    @Test
    public void testJdbc_H2_Test() {
        testJdbcTemplate("jdbc:h2:~/test");
    }

    private void testJdbcTemplate(String url) {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl(url);
        dataSource.setPassword("");

        testJdbcTemplate(dataSource);
    }

    private void testJdbcTemplate(SimpleDriverDataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table customers if exists");
        jdbcTemplate.execute(
                "create table customers (id serial, first_name varchar(255), last_name varchar(255))");

        String[] names = "John Woo;Jeff Dean;Josh Bloch;Josh Long".split(";");
        for (String fullname : names) {
            String[] name = fullname.split(" ");
            System.out.printf("Inserting customer record for %s %s\n", name[0], name[1]);
            jdbcTemplate.update(
                    "INSERT INTO customers (first_name, last_name) VALUES (?, ?)", name[0], name[1]);
        }

        System.out.println("Querying for customer records where first_name = 'Josh':");
        List<Customer> results = jdbcTemplate.query(
                "select * from customers where first_name = ?",
                new Object[] { "Josh" },
                new RowMapper<Customer>() {
                    @Override
                    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Customer(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"));
                    }
                }
        );

        for (Customer customer : results) {
            System.out.println(customer);
        }
    }

}
