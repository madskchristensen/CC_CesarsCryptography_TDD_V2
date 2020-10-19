import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {

    static String allowedCharacters;
    static String allowedExtraCharacters;
    static Map<Character, Character> characterMap;

    @BeforeAll
    static void init() {
        allowedCharacters = "abcdefghijklmnopqrstuvwxyzæøå";
        allowedExtraCharacters = "[!@#$%&*()_+=|<>?{}\\[]~-],.;: ";
    }

    // Logikken går i stykker hvis key er for høj. F.eks. 28. Kan det fikses?
    @BeforeEach
    public void prepareMapWithKey3() {
        characterMap = new HashMap<>();

        int j = 0;
        int key = 3;
        int boundary = allowedCharacters.length() - key;

        for (int i = 0; i < allowedCharacters.length(); i++) {
            if (i >= boundary) {
                characterMap.put(allowedCharacters.charAt(i), allowedCharacters.charAt(j));
                j++;
            } else {
                characterMap.put(allowedCharacters.charAt(i), allowedCharacters.charAt(i + key));
            }
        }
    }

    @Test
    void hasMapBeenPopulatedCorrectlyFromAllowedCharactersString() {
        assertAll("characterMap",
                () -> assertEquals('æ', characterMap.get('x')),
                () -> assertEquals('d', characterMap.get('a')),
                () -> assertEquals('p', characterMap.get('m')),
                () -> assertEquals('y', characterMap.get('v')),
                () -> assertEquals('v', characterMap.get('s'))
        );
    }

    @Test
    void readFirstSecondAndLastWordFromTextFile() {
        File file = null;
        Scanner scanner = null;

        try {
            file = new File("resources/aliceinwonderland.txt");
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String firstWord = scanner.next();
        String secondWord = scanner.next();
        String aliceInWonderland = firstWord + " " + secondWord;

        while (scanner.hasNext()) {
            aliceInWonderland = aliceInWonderland + " " + scanner.next();
        }

        assertEquals("The", firstWord);
        assertEquals("Project", secondWord);
        assertEquals("eBooks.", aliceInWonderland.substring(aliceInWonderland.lastIndexOf(" ") + 1));
    }

    @Test
    void encryptStringUsingCharacterMap() {
        String stringToEncrypt = "Hej+[ meD]}3,45 dig15.13¤";
        StringBuilder encrpytedString = new StringBuilder();

        for (int i = 0; i < stringToEncrypt.length(); i++) {
            char currentChar = stringToEncrypt.charAt(i);

            // sæt state på booleans der beskriver den nuværende char
            boolean isNumber = Character.isDigit(currentChar);
            boolean isUpperCase = Character.isUpperCase(currentChar);
            boolean isAllowedCharacter = allowedCharacters.contains("" + Character.toLowerCase(currentChar));
            boolean isAllowedExtra = allowedExtraCharacters.contains("" + currentChar);

            // udfør operation alt efter state på booleans
            if (isNumber) {
                encrpytedString.append(currentChar);

            } else if (isAllowedExtra) {
                encrpytedString.append(currentChar);

            } else if (isUpperCase && isAllowedCharacter) {
                char encryptedChar = characterMap.get(Character.toLowerCase(currentChar));
                encrpytedString.append(Character.toUpperCase(encryptedChar));
                // oneliner som gør det samme: encrpytedString.append(Character.toUpperCase(characterMap.get(Character.toLowerCase(currentChar))));

            } else if (isAllowedCharacter) {
                encrpytedString.append(characterMap.get(currentChar));

            } else {
                encrpytedString.append("\u25A1");
            }
        }

        assertEquals("Khm+[ phG]}3,45 glj15.13□", encrpytedString.toString());
    }
}