package starter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Function;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

   public static String postXML(String uri,String xmlData){
       return postData(uri,xmlData,"application/xml","application/xml");
   }

    public static String postData(String uri, String data, String acceptHeader, String contentTypeHeader) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(uri);
            StringEntity entity = new StringEntity(data);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", acceptHeader);
            httpPost.setHeader("Content-type", contentTypeHeader);
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}