import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String keyString = "000102030405060708090a0b0c0d0e0f";
        String plaintextString = "00112233445566778899aabbccddeeff";
        Incr cipher = new Incr();
        byte[][] state = cipher.encryptparsestring(plaintextString, keyString);
        Dec decipher = new Dec(cipher.getKeyExpansionresult());
        decipher.decryptstate(state);
    }
}
