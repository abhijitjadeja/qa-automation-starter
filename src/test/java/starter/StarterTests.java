package starter;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static starter.AutomationTestUtils.*;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

@SpringJUnitConfig(TestConfig.class)
@SqlConfig(dataSource = "applicationDataSource")
class StarterTests {
	@Resource(name = "applicationNamedParameterJdbcTemplate")
	NamedParameterJdbcTemplate pdrNamedParameterJdbcTemplate;

	@Test
	@Sql("/test.sql")
	@DisplayName("web service call")
	void callWebService() {

		int rows = countRowsInTableWhere(pdrNamedParameterJdbcTemplate.getJdbcTemplate(), "PERSON", "PERSON_ID=1");

		assertEquals(1, rows);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("PERSON_ID", 1);
		Map<String, Object> person = pdrNamedParameterJdbcTemplate
				.queryForMap("SELECT * FROM PERSON WHERE PERSON_ID=:PERSON_ID", parameters);
		assertEqualsInMap(person, "PERSON_NAME", "Jane Doe");
		assertEqualsInMap(person, "EMP_ID", 1001);

	}

	@ParameterizedTest(name = "{0} + {1} = {2}")
	@CsvSource({ //
			"0,    1,   1", //
			"1,    2,   3", //
			"49,  51, 100", //
			"1,  100, 101" })
	void runWithParameters(int first, int second, int expectedResult) {

	}
}
