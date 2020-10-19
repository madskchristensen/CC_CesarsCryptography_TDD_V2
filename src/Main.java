import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final File alice = new File("resources/aliceinwonderland.txt");
        Crypto crypto = new Crypto();

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("encryptedAlice.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.write(crypto.readFile(alice));
            // writer.write(crypto.encrypt(alice));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}