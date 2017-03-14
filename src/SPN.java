import java.util.Map;

/**
 * Created by Sacha on 10.3.17.
 */

public class SPN {

    private int r, n, m, s;
    private Map<Integer, Integer> sBox, perm;

    public SPN(int r, int n, int m, Map<Integer, Integer> sBox, Map<Integer, Integer> perm, int s) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.sBox = sBox;
        this.perm = perm;
        this.s = s;
    }

    public String encrypt(String source, Byte[] key) {
        return "";
    }

    public String decrypt(String cypher, Byte[] key) {
        return "";
    }

}
