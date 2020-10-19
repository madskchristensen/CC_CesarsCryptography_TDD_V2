import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// todo -- Generer allowedExtraCharacters automatisk ud fra fil (se printUniqueCharacters metoden)
// todo -- Find ud af om det ovenstående overhovedet giver mening
// todo -- Gør så formatering af endelig fil matcher formatering fra oprindelig
// todo -- Det sidste punktum kommer ikke med i den krypterede fil :(

public class Crypto {
    private final String allowedLetters = "abcdefghijklmnopqrstuvwxyz";
    private final String allowedExtraCharacters = "[!@#$%&*()_+=|<>?{}\\[]~-],.;:\"'/ ";
    private Map<Character, Character> characterMap;
    private final int key;

    public Crypto() {
        this.key = 3;

        populateMap();
    }

    public Crypto(int key) {
        this.key = key;

        populateMap();
    }

    // https://stackoverflow.com/questions/6319775/java-collections-convert-a-string-to-a-list-of-characters
    // Kan bruges senere til at generere en dynamisk liste af tilladte karakterer
    public void printUniqueCharacters(File file) {
        String stringToCheck = readFile(file);

        Set<Character> uniqueCharacterSet = stringToCheck.
                chars()
                .mapToObj(i -> (char) i).collect(Collectors.toCollection(TreeSet::new));

        System.out.println(uniqueCharacterSet);

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(uniqueCharacterSet.toString());

        System.out.println(m);
    }

    // IKKE FÆRDIG
    public String readFileFormatted(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bf = null;

        return "";
    }

    public String encrypt(File file) {
        String stringToEncrypt = readFile(file);

        StringBuilder encrpytedString = new StringBuilder();

        for (int i = 1; i < stringToEncrypt.length(); i++) {
            char currentChar = stringToEncrypt.charAt(i);

            // sæt state på booleans der beskriver den nuværende char
            boolean isNumber = Character.isDigit(currentChar);
            boolean isUpperCase = Character.isUpperCase(currentChar);
            boolean isAllowedCharacter = allowedLetters.contains("" + Character.toLowerCase(currentChar));
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

        return encrpytedString.toString();
    }

    public String readFile(File file) {
        String stringToReturn = "";
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert scanner != null;

        while(scanner.hasNext()) {
            stringToReturn = stringToReturn + " " + scanner.next();
        }

        System.out.println(stringToReturn);

        return stringToReturn;
    }

    private void populateMap() {
        characterMap = new HashMap<>();

        int j = 0;
        int boundary = allowedLetters.length() - key;

        for (int i = 0; i < allowedLetters.length(); i++) {
            if (i >= boundary) {
                characterMap.put(allowedLetters.charAt(i), allowedLetters.charAt(j));
                j++;
            } else {
                characterMap.put(allowedLetters.charAt(i), allowedLetters.charAt(i + key));
            }
        }
    }
}