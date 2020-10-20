import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final File alice = new File("resources/aliceinwonderland.txt");
        Crypto crypto = new Crypto();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("encryptedAlice.txt"));
            writer.write(crypto.encrypt(alice));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}