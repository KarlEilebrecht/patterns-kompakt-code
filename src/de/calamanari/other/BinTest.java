package de.calamanari.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class BinTest {

    @Test
    public void testBinaryCodes() throws Exception {
        
        Map<Integer, String> used = new HashMap<>();
        
        //used.add("00");
        //used.add("01");
        //used.add("10");
        
         
        for (int i = 0; i < 255; i++) {
            boolean codeInUse = false;
            String code = null;
            int offset = 0;
            do {
                codeInUse = false;
                code = Integer.toBinaryString(offset + i);
                if (code.length() < 2) {
                    code = "00" + code;
                }
                code = "11" + code;
                System.out.println(code);
                int result = checkCodeIsInUse(i, used, code);
                if (result > -1) {
                    codeInUse = true;
                    offset = offset + result;
                }
            }
            while (codeInUse);
            used.put(i, code);
        }

        for (int i = 0; i < 255; i++) {
            System.out.println("" + i + " --> " + used.get(i));
        }
        
        
        
    }

    
    private int checkCodeIsInUse(int currentIndex, Map<Integer, String> usedCodes, String code) {

        for (int i = currentIndex-1; i > -1; i--) {
            String used = usedCodes.get(i);
            if (code.startsWith(used)) {
                return i;
            }
            
        }
               
        return -1;
    }
    
}
