package starter;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.*;
@Configuration
public class TestConfig {

    @Bean("pdr")
    public DataSource dataSource(){
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setUrl("jdbc:hsqldb:mem:qa");
        return jdbcDataSource;
    }
}