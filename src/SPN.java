import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Developed by Sacha Schmid, Tobias Baumgartner, Rinesch Murugathas.
 */

public class SPN {

    private int r, n, m, s;
    private Map<String, String> sBox;
    private Map<Integer, Integer> perm;

    public SPN(int r, int n, int m, Map<String, String> sBox, Map<Integer, Integer> perm, int s) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.sBox = sBox;
        this.perm = perm;
        this.s = s;
    }

    public String encrypt(String source, String key) {

        // Check if the key has a valid length.

        if (key.length() != s) {
            throw new IllegalArgumentException();
        }

        // Optionally pad the string so it can be encrypted properly.

        if (source.length() % (n * m) != 0) {
            source += "1";
        }

        while (source.length() % (n * m) != 0) {
            source += "0";
        }

        // For testing purposes, use "0000010011010010".

        String yMinusOne = randomY(n * m);

        // Split the source text into parts of length n * m.

        List<String> parts = new ArrayList<>();

        while (source.length() > 0) {
            parts.add(source.substring(0, n * m));
            source = source.substring(n * m, source.length());
        }

        // Transform the parts of the string.

        List<String> transformed = new ArrayList<>();

        // y-1 is always included as the first part of the encrypted string.

        transformed.add(yMinusOne);

        parts.forEach(p -> {
            transformed.add(encryptBlock(p, key, yMinusOne, transformed.size() - 1));
        });

        // Return all the parts of the encrypted string, concatenated to a
        // single string.

        return transformed.stream().collect(Collectors.joining());
    }

    public String decrypt(String cypher, String key) {

        // Check if the key has a valid length.

        if (key.length() != s) {
            throw new IllegalArgumentException();
        }

        // Get the first part of the cypher text. It always represents y-1.

        String yMinusOne = cypher.substring(0, n * m);
        cypher = cypher.substring(n * m, cypher.length());

        // Split the cypher text into parts of length n * m.

        List<String> parts = new ArrayList<>();

        while (cypher.length() > 0) {
            parts.add(cypher.substring(0, n * m));
            cypher = cypher.substring(n * m, cypher.length());
        }

        // Transform the parts of the string.

        List<String> transformed = new ArrayList<>();

        parts.forEach(p -> {
            transformed.add(encryptBlock(p, key, yMinusOne, transformed.size()));
        });

        // Return all the parts of the decrypted string, concatenated to a
        // single string.

        return transformed.stream().collect(Collectors.joining());
    }

    private String encryptBlock(String p, String key, String yMinusOne, int index) {

        // Calculate ((y-1 + i) mod 2^l).
        // In order to do this, convert bitstring y-1 to an integer, do the
        // calculations and convert back to a bitstring.

        String curY = yMinusOne;
        int curYAsInt = intify(curY);

        curYAsInt += index;
        curYAsInt %= Math.pow(2, n * m);
        curY = bitstringify(curYAsInt);

        // Do the first step of the SPN transformation.

        String part = xor(curY, key.substring(0, n * m));

        // Execute all the round steps using sBox transformation, bit
        // permutation, and xor.

        for (int i = 1; i < r; i++) {
            part = sTransform(part);
            part = permutate(part);
            part = xor(part, key.substring(n * i, (n * m) + n * i));
        }

        // Do the final step of the SPN transformation.

        part = sTransform(part);
        part = xor(part, key.substring(n * r, (n * m) + n * r));

        // Append the current part to the encrypted string.

        return xor(part, p);
    }

    // Do a simple char-by-char comparison between two given strings.

    public String xor(String a, String b) {

        // Validate that both strings have the same length, otherwise we cannot
        // xor them.

        if (a.length() != b.length()) {
            throw new IllegalArgumentException();
        }

        StringBuilder afterXOR = new StringBuilder();

        for (int i = 0; i < a.length(); i++) {
            afterXOR.append(a.charAt(i) != b.charAt(i) ? '1' : '0');
        }

        return afterXOR.toString();
    }

    // Perform a sBox transformation of a given string.

    private String sTransform(String a) {

        // Split the source string into parts that can be used for sBox
        // transformation.

        String[] splitted = new String[n];
        StringBuilder afterBox = new StringBuilder();

        for (int i = 0; i < n; i++) {
            splitted[i] = a.substring(i * n, i * n + m);
        }

        // Do a simple sBox transformation using the given HashMap.

        for (int i = 0; i < splitted.length; i++) {
            afterBox.append(sBox.get(splitted[i]));
        }

        return afterBox.toString();
    }

    // Move the bits from their source position to the destination position.

    private String permutate(String a) {
        StringBuilder after = new StringBuilder(a);

        for (int i = 0; i < 16; i++) {
            after.setCharAt(perm.get(i), a.charAt(i));
        }

        return after.toString();
    }

    // Generate a random bitstring of a given length.

    private String randomY(int length) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(r.nextInt(2));
        }

        return sb.toString();
    }

    // Convert a bitstring to an integer.

    private int intify(String bitstring) {
        return Integer.parseInt(bitstring, 2);
    }

    // Convert an integer to a bitstring using a simple algorithm.

    private String bitstringify(int num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.insert(0, num % 2 == 1 ? '1' : '0');
            num = (int) Math.floor(num / 2);
        }

        // Pad the string from the left if it does not have the required length.

        while (sb.length() % (n * m) != 0) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }
}
