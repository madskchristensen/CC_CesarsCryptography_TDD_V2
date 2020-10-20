import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// todo -- 2. Generer allowedExtraCharacters automatisk ud fra fil (se printUniqueCharacters metoden)
// todo -- 2.1 Find ud af om det ovenstående overhovedet giver mening

public class Crypto {
    private final String allowedLetters = "abcdefghijklmnopqrstuvwxyz";
    private final String allowedExtraCharacters = "[!@#$%&*()_+=|<>?{}\\[]~-],.;:\"'/ ";
    private Map<Character, Character> characterEncryptionMap;
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
/*    public void printUniqueCharacters(File file) {
        String stringToCheck = readFile(file);

        Set<Character> uniqueCharacterSet = stringToCheck.
                chars()
                .mapToObj(i -> (char) i).collect(Collectors.toCollection(TreeSet::new));

        System.out.println(uniqueCharacterSet);

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(uniqueCharacterSet.toString());

        System.out.println(m);
    }*/

    // Utility metode til at læse en fil. Selve metoden bruges ikke til den endelige løsning pt.
    public String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

            for(String line : lines) {
                if(line.isBlank()) {
                    stringBuffer.append(System.lineSeparator());
                } else {
                    stringBuffer.append(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    // https://javarevisited.blogspot.com/2015/02/how-to-read-file-in-one-line-java-8.html
    // https://beginnersbook.com/2015/04/append-a-newline-to-stringbuffer/
    public String encrypt(File file) {
        StringBuilder encrpytedString = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

            for(String line : lines) {
                    for (int i = 0; i < line.length(); i++) {
                        char currentChar = line.charAt(i);

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
                            char encryptedChar = characterEncryptionMap.get(Character.toLowerCase(currentChar));
                            encrpytedString.append(Character.toUpperCase(encryptedChar));

                        } else if (isAllowedCharacter) {
                            encrpytedString.append(characterEncryptionMap.get(currentChar));

                        } else {
                            encrpytedString.append("\u25A1");
                        }
                    }

                    // check om sidste index i list er nået.
                    // hvis ikke skal der laves ny linje
                    if(lines.indexOf(line) != lines.size() - 1) {
                        encrpytedString.append(System.lineSeparator());
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return encrpytedString.toString();
    }

    private void populateMap() {
        characterEncryptionMap = new HashMap<>();

        int j = 0;
        int boundary = allowedLetters.length() - key;

        for (int i = 0; i < allowedLetters.length(); i++) {
            if (i >= boundary) {
                characterEncryptionMap.put(allowedLetters.charAt(i), allowedLetters.charAt(j));
                j++;
            } else {
                characterEncryptionMap.put(allowedLetters.charAt(i), allowedLetters.charAt(i + key));
            }
        }
    }
}