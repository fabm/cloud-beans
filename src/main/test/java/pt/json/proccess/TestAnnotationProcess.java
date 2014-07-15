package java.pt.json.proccess;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.yaml.snakeyaml.Yaml;
import pt.gapiap.proccess.AnnotationProcessor;
import pt.gapiap.proccess.Bundle;
import pt.gapiap.proccess.validation.annotations.Email;
import pt.gapiap.proccess.validation.annotations.Min;
import pt.gapiap.proccess.validation.annotations.Required;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

import static com.google.common.collect.ImmutableMap.of;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAnnotationProcess {

    @Test
    public void annotationProcessor() {


        AnnotationProcessor annotationProcessor = mock(AnnotationProcessor.class);
        annotationProcessor.aliasBundle = mock(Bundle.class);


        final Map<String, String> map = new HashMap<>();
        map.put("userBH.update.email", "e-mail");
        map.put("article.create.body", "corpo");

        when(annotationProcessor.aliasBundle.keySet()).thenReturn(map.keySet());
        when(annotationProcessor.aliasBundle.getString(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return map.get(invocationOnMock.getArguments()[0]);
            }
        });

        when(annotationProcessor.aliasMap()).thenCallRealMethod();
        Map<String, ?> mapOut = annotationProcessor.aliasMap();

        Map<String, ?> mapOutExpected = of(
                "userBH",
                of(
                        "update",
                        of(
                                "email",
                                "e-mail"
                        )
                ),
                "article",
                of(
                        "create",
                        of(
                                "body",
                                "corpo"
                        )
                )
        );


        assertEquals(mapOutExpected, mapOut);
    }

    @Test
    public void loadToYaml() throws FileNotFoundException {

        Yaml yaml = new Yaml();
        URL url = getClass().getResource("validators.yml");
        List list = new ArrayList();
        Map map = new HashMap();

        map.put("class", Email.class.getName());
        map.put("alias", "email");
        map.put("vars", null);
        list.add(map);

        map = new HashMap();
        map.put("class", Min.class.getName());
        map.put("alias", "min");
        map.put("vars", of("value", "number"));
        list.add(map);

        System.out.println(yaml.dump(list));
    }

    @Test
    public void setSameClassInHashSet(){
        Set<Class<? extends Annotation>> set = new HashSet<>();
        set.add(Required.class);
        assertFalse(set.add(Required.class));
    }

    @Test
    public void loadFromYaml() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        URL url = getClass().getResource("validators.yml");
        System.out.println(yaml.load(new FileReader(url.getFile())));
    }
}
