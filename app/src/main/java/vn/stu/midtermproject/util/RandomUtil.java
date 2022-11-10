package vn.stu.midtermproject.util;

public class RandomUtil {
    public static String getAlphaNumericString(Integer n) {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
