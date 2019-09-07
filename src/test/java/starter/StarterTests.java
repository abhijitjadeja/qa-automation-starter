package starter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static starter.AutomationTestUtils.assertEqualsInMap;
import static starter.AutomationTestUtils.populateTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;

@SpringJUnitConfig(TestConfig.class)
@SqlConfig(dataSource = "applicationDataSource")
class StarterTests {
	@Resource(name = "applicationNamedParameterJdbcTemplate")
	NamedParameterJdbcTemplate pdrNamedParameterJdbcTemplate;
	@Resource(name = "freemarkerTemplateConfiguraton")
	freemarker.template.Configuration freemarkerTemplateConfiguration;

	@Test
	@Sql("/sql/test.sql")
	@DisplayName("web service call")
	void callWebService() throws IOException, TemplateException {

		int rows = countRowsInTableWhere(pdrNamedParameterJdbcTemplate.getJdbcTemplate(), "PERSON", "PERSON_ID=1");

		assertEquals(1, rows);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("PERSON_ID", 1);
		Map<String, Object> person = pdrNamedParameterJdbcTemplate
				.queryForMap("SELECT * FROM PERSON WHERE PERSON_ID=:PERSON_ID", parameters);
		assertEqualsInMap(person, "PERSON_NAME", "Jane Doe");
		assertEqualsInMap(person, "EMP_ID", 1001);
		Map<String, String> templateParameters = Collections.singletonMap("firstData", "My first test data");
		String populatedTemplate = populateTemplate("webservice.xml", templateParameters,
				freemarkerTemplateConfiguration);
		assertEquals("<test>My first test data</test>", populatedTemplate);
	}

	@ParameterizedTest(name = "testWebServiceWithParameters")
	@CsvFileSource(resources = "/testdata/parameters.csv")
	void runWithParameters(String ssn, String firstName, String lastName, String dob) throws SAXException, IOException {
		Map<String, String> templateParameters = new HashMap<>(4);
		templateParameters.put("ssn", ssn);
		templateParameters.put("firstName", firstName);
		templateParameters.put("lastName", lastName);
		templateParameters.put("dob", dob);
		String populatedTemplate = populateTemplate("webservice-withparameters.xml", templateParameters,
				freemarkerTemplateConfiguration);
		XMLUnit.setIgnoreWhitespace(true);
		Diff d = new Diff("<test>		<ssn>" + ssn + "</ssn>		<firstName>" + firstName
				+ "</firstName>		<lastName>" + lastName + "</lastName>		<dob>" + dob + "</dob>		</test>",
				populatedTemplate);
		System.out.println(d);
		assertTrue(d.identical());
	}
}
