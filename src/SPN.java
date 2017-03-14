import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sacha on 10.3.17.
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
        String bitstring = bitstringify(source);

        if (bitstring.length() % (n * m) != 0) {
            bitstring += "1";
        }

        while (bitstring.length() % (n * m) != 0) {
            bitstring += "0";
        }

        List<String> parts = new ArrayList<>();

        while (bitstring.length() > 0) {
            parts.add(bitstring.substring(0, n * m));
            bitstring = bitstring.substring(n * m, bitstring.length());
        }

        List<String> transformed = new ArrayList<>();

        parts.forEach(p -> {
            String part = xor(p, key.substring(0, n * m - 1));

            for (int i = 1; i < r; i++) {
                part = sTransform(part);
                part = permutate(part);
                part = xor(part, key.substring(n * i, (n * m - 1) + n * i));
            }

            part = sTransform(part);
            part = xor(part, key.substring(n * r, (n * m - 1) + n * r));

            transformed.add(part);
        });

        return transformed.toString();
    }

    public String decrypt(String cypher, String key) {
        return "";
    }

    public String xor(String a, String b) {

        if (a.length() != b.length()) {
            throw new IllegalArgumentException();
        }

        StringBuilder afterXOR = new StringBuilder();

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                afterXOR.append("1");
            } else {
                afterXOR.append("0");
            }
        }

        System.out.println("XOR passed. Input: " +a +" | " +b +" Output: " +afterXOR.toString());
        return afterXOR.toString();
    }

    public String sTransform(String a) {
        String[] splitted = new String[4];
        StringBuilder afterBox = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            splitted[i] = a.substring(i*4,i*4+4);
        }
        for (int i = 0; i < splitted.length; i++) {
            afterBox.append(sBox.get(splitted[i]));
        }
        System.out.println("sBox passed. Input: " +a +" Output: " +afterBox.toString());
        return afterBox.toString();
    }

    public String permutate(String a) {
        StringBuilder after = new StringBuilder(a);

        for (int i = 0; i < 16; i++) {
            after.setCharAt(perm.get(i), a.charAt(i));
        }

        System.out.println("Perm passed. Input: " +a + " Output: " +after.toString());

        return after.toString();
    }

    public String bitstringify(String source) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            StringBuilder r = new StringBuilder();

            int j = (int) source.charAt(i);
            for (int k = 0; k < 8; k++) {
                if ((j % 2) == 1) {
                    r.insert(0, '1');
                } else {
                    r.insert(0, '0');
                }
                j = j / 2;
            }

            s.append(r.toString());
        }

        return s.toString();
    }
}
