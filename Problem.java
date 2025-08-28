import java.io.FileReader;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Problem {

    public static BigInteger decodeBig(String val, int base) {
        return new BigInteger(val, base);
    }

    public static BigDecimal interpolate(List<BigInteger[]> pts) {
        int n = pts.size();
        BigDecimal constant = BigDecimal.ZERO;

        for (int i = 0; i < n; i++) {
            BigDecimal xi = new BigDecimal(pts.get(i)[0]);
            BigDecimal yi = new BigDecimal(pts.get(i)[1]);
            BigDecimal li = BigDecimal.ONE;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    BigDecimal xj = new BigDecimal(pts.get(j)[0]);
                    li = li.multiply(BigDecimal.ZERO.subtract(xj)
                            .divide(xi.subtract(xj), 50, RoundingMode.HALF_UP));
                }
            }

            constant = constant.add(yi.multiply(li));
        }

        return constant;
    }

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject input = (JSONObject) parser.parse(new FileReader("input.json"));
            JSONObject keys = (JSONObject) input.get("keys");
            long n = (long) keys.get("n");
            long k = (long) keys.get("k");

            List<BigInteger[]> pts = new ArrayList<>();

            for (Object key : input.keySet()) {
                String kStr = (String) key;
                if (kStr.equals("keys")) continue;

                BigInteger x = new BigInteger(kStr);
                JSONObject yObj = (JSONObject) input.get(kStr);
                int base = Integer.parseInt((String) yObj.get("base"));
                String val = (String) yObj.get("value");

                BigInteger y = decodeBig(val, base);
                pts.add(new BigInteger[]{x, y});
            }

            BigDecimal c = interpolate(pts);

            System.out.println("Secret constant (c): " + c.toPlainString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
