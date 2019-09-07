package starter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import freemarker.cache.ClassTemplateLoader;

@Configuration
public class TestConfig {

    @Bean("applicationDataSource")
    public DataSource dataSource() {
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setUrl("jdbc:hsqldb:mem:qa;sql.syntax_ora=true");
        return jdbcDataSource;
    }

    @Bean("applicationNamedParameterJdbcTemplate")
    @Resource(name = "applicationDataSource")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource pdrDataSource) {
        return new NamedParameterJdbcTemplate(pdrDataSource);
    }

    @Bean("freemarkerTemplateConfiguraton")
    public freemarker.template.Configuration getFreemarkerTemplateConfiguration() {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.29) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        freemarker.template.Configuration cfg = new freemarker.template.Configuration();

        // From here we will set the settings recommended for new projects. These
        // aren't the defaults for backward compatibilty.

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/templates"));
        return cfg;
    }
}