package starter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Function;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AutomationTestUtils {

    public static void assertEqualsInMap(Map<String, Object> row, String fieldName, String expectedValue) {
        Function<Object, String> typeConverter = value -> {
            if (value instanceof String) {
                return (String) value;
            } else {
                throw new RuntimeException("expected string but found. The object was " + value.getClass());
            }
        };

        assertEqualsInMap(row, fieldName, expectedValue, typeConverter);
    }

    private static <T> Function<Object, T> objectToNumber(Function<Number, T> numberToType) {
        return value -> {
            if (value instanceof Number) {
                Number numValue = (Number) value;
                return numberToType.apply(numValue);
            } else {
                throw new RuntimeException("expected Number type but found " + value.getClass());
            }
        };
    }

    public static void assertEqualsInMap(Map<String, Object> row, String fieldName, int expectedValue) {

        assertEqualsInMap(row, fieldName, expectedValue, objectToNumber(Number::intValue));

    }

    public static void assertEqualsInMap(Map<String, Object> row, String fieldName, long expectedValue) {

        assertEqualsInMap(row, fieldName, expectedValue, objectToNumber(Number::longValue));

    }

    public static String populateTemplate(String templateName, Map<String, ?> parameters,
            Configuration templateConfiguration) {
        try {
            Template t = templateConfiguration.getTemplate(templateName);
            StringWriter stringWriter = new StringWriter(2000);
            t.process(parameters, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> void assertEqualsInMap(Map<String, Object> row, String fieldName, T expectedValue,
            Function<Object, T> typeConverter) {
        Object value = row.get(fieldName);
        if (value != null) {
            T typedValue = typeConverter.apply(value);
            assertEquals(expectedValue, typedValue);

        } else {
            fail("object was null");
        }
    }
}