package data_processing;

public class Utils {

    public static Object checkExist(Object o) {
        if (o == null) {
            return null;
        } else if (o.equals(null)) {
            return null;
        } else {
            return o;
        }
    }
}
