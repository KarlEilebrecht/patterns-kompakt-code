package de.calamanari.other;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class RegExpTest {

    @Ignore
    @Test
    public void test() {
        // Pattern p = Pattern.compile("^(import[\\s]+(static)?)?[\\s]+([\\w]+\\.)*[\\w]+(\\.\\*)?[\\s]*$");
        Pattern p = Pattern.compile("^\\s*(import\\s+(static\\s+)?)?((\\w+\\.)+\\w+)(\\.\\*)?(\\s*;\\s*)?$");
        String source = "  import de.test.gaga ; ";
        Matcher m = p.matcher(source);
        assertTrue(m.find());

        System.out.println(m.groupCount());
        for (int i = 0; i < m.groupCount(); i++) {
            System.out.println(m.group(i));
        }

        String[] pathFrom = new String[] {};
        String[] pathTo = new String[] {};

        String className = "Raba";

        System.out.println(JavaDocSourceTransformer.createHtmlRefFrom(pathFrom, pathTo, className));

    }

}
