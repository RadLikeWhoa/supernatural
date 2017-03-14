import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sacha on 10.3.17.
 */
public class SPNTest {
    public static void main(String[] args) {
        Map<Integer, Integer> sBox = new HashMap<>();
        Map<Integer, Integer> perm = new HashMap<>();

        sBox.put(0, 14);
        sBox.put(1, 4);
        sBox.put(2, 13);
        sBox.put(3, 1);
        sBox.put(4, 2);
        sBox.put(5, 15);
        sBox.put(6, 11);
        sBox.put(7, 8);
        sBox.put(8, 3);
        sBox.put(9, 10);
        sBox.put(10, 6);
        sBox.put(11, 12);
        sBox.put(12, 5);
        sBox.put(13, 9);
        sBox.put(14, 0);
        sBox.put(15, 7);

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

        System.out.println((int) 'a');

        SPN s = new SPN(4, 4, 4, sBox, perm, 32);
        Byte[] key = new Byte[] { 0b0011, 0b1010, 0b1001, 0b0100, 0b1101, 0b0110, 0b0011, 0b1111 };

        System.out.println(s.encrypt("abc", key));
    }
}
