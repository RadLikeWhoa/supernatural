import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sacha on 10.3.17.
 */
public class SPNTest {
    public static void main(String[] args) {
        Map<String, String> sBox = new HashMap<>();
        Map<Integer, Integer> perm = new HashMap<>();

        sBox.put("0000", "1110");
        sBox.put("0001", "0100");
        sBox.put("0010", "1101");
        sBox.put("0011", "0001");
        sBox.put("0100", "0010");
        sBox.put("0101", "1111");
        sBox.put("0110", "1011");
        sBox.put("0111", "1000");
        sBox.put("1000", "0011");
        sBox.put("1001", "1010");
        sBox.put("1010", "0110");
        sBox.put("1011", "1100");
        sBox.put("1100", "0101");
        sBox.put("1101", "1001");
        sBox.put("1110", "0000");
        sBox.put("1111", "0111");

        perm.put(0, 0);
        perm.put(1, 4);
        perm.put(2, 8);
        perm.put(3, 12);
        perm.put(4, 1);
        perm.put(5, 5);
        perm.put(6, 9);
        perm.put(7, 13);
        perm.put(8, 2);
        perm.put(9, 6);
        perm.put(10, 10);
        perm.put(11, 14);
        perm.put(12, 3);
        perm.put(13, 7);
        perm.put(14, 11);
        perm.put(15, 15);

        SPN s = new SPN(4, 4, 4, sBox, perm, 32);

        // System.out.println(s.encrypt("abc", "00111010100101001101011000111111"));
        s.sTransform("1111000011110000");
        s.permutate("1111000011110000");
        s.xor("0100", "1111");
    }
}
