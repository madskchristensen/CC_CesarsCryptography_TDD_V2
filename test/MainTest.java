import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    static String allowedCharacters;

    @BeforeAll
    static void init() {
        allowedCharacters = "abcdefghijklmnopqrstuvxyz";
    }

    @Test
    void populateMapFromAllowedCharacters() {
        Map<Character, Character> characterMap = new HashMap<>();

        int j = 0;
        int boundary = allowedCharacters.length() - 3;

        for(int i = 0; i < allowedCharacters.length(); i++) {
            if(i >= boundary) {
                characterMap.put(allowedCharacters.charAt(i), allowedCharacters.charAt(j));
                j++;
            } else {
                characterMap.put(allowedCharacters.charAt(i), allowedCharacters.charAt(i + 3));
            }
        }

        assertAll("characterMap",
                () -> assertEquals('a', characterMap.get('x')),
                () -> assertEquals('d', characterMap.get('a')),
                () -> assertEquals('p', characterMap.get('m')),
                () -> assertEquals('z', characterMap.get('v')),
                () -> assertEquals('v', characterMap.get('s'))
        );
    }
}