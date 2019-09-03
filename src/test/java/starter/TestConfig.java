package starter;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class TestConfig {

    @Bean("applicationDataSource")
    public DataSource dataSource(){
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setUrl("jdbc:hsqldb:mem:qa;sql.syntax_ora=true");
        return jdbcDataSource;
    }

    @Bean("applicationNamedParameterJdbcTemplate")
    @Resource(name="applicationDataSource")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource pdrDataSource){
        return new NamedParameterJdbcTemplate(pdrDataSource);
    }
}