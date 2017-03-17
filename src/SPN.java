import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
        if (source.length() % (n * m) != 0) {
            source += "1";
        }

        while (source.length() % (n * m) != 0) {
            source += "0";
        }

        // THIS IS y-1
        String yn = "0000010011010010";

        List<String> parts = new ArrayList<>();

        while (source.length() > 0) {
            parts.add(source.substring(0, n * m));
            source = source.substring(n * m, source.length());
        }

        List<String> transformed = new ArrayList<>();

        transformed.add(yn);

        parts.forEach(p -> {
            String curY = yn;
            int curYAsInt = intify(curY);

            curYAsInt += transformed.size() - 1;
            curYAsInt %= Math.pow(2, n * m);
            curY = bitstringify(curYAsInt);

            String part = xor(curY, key.substring(0, n * m));

            for (int i = 1; i < r; i++) {
                part = sTransform(part);
                part = permutate(part);
                part = xor(part, key.substring(n * i, (n * m) + n * i));
            }

            part = sTransform(part);
            part = xor(part, key.substring(n * r, (n * m) + n * r));

            transformed.add(xor(part, p));
        });

        return transformed.stream().collect(Collectors.joining());
    }

    public String decrypt(String cypher, String key) {
        String yn = cypher.substring(0, n * m);
        cypher = cypher.substring(n * m, cypher.length());

        List<String> parts = new ArrayList<>();

        while (cypher.length() > 0) {
            parts.add(cypher.substring(0, n * m));
            cypher = cypher.substring(n * m, cypher.length());
        }

        List<String> transformed = new ArrayList<>();

        parts.forEach(p -> {
            String curY = yn;
            int curYAsInt = intify(curY);

            curYAsInt += transformed.size();
            curYAsInt %= Math.pow(2, n * m);
            curY = bitstringify(curYAsInt);

            String part = xor(curY, key.substring(0, n * m));

            for (int i = 1; i < r; i++) {
                part = sTransform(part);
                part = permutate(part);
                part = xor(part, key.substring(n * i, (n * m) + n * i));
            }

            part = sTransform(part);
            part = xor(part, key.substring(n * r, (n * m) + n * r));

            transformed.add(xor(part, p));
        });

        return transformed.stream().collect(Collectors.joining());
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

        return afterBox.toString();
    }

    public String permutate(String a) {
        StringBuilder after = new StringBuilder(a);

        for (int i = 0; i < 16; i++) {
            after.setCharAt(perm.get(i), a.charAt(i));
        }

        return after.toString();
    }

    private String randomY(int length) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(r.nextInt(1));
        }

        return sb.toString();
    }

    private int intify(String bitstring) {
        return Integer.parseInt(bitstring, 2);
    }

    private String bitstringify(int num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            sb.insert(0, num % 2 == 1 ? '1' : '0');
            num = (int) Math.floor(num / 2);
        }

        while (sb.length() % (n * m) != 0) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }
}
