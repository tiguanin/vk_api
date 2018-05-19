package data_processing;

import org.json.simple.JSONObject;

public class Utils {

    /**
     * Проверка наличия искомого ключа в JSON-объекте.
     */
    public static Object checkExist(String key, JSONObject params) {
        if (params.containsKey(key)) {
            // TODO: костыль, но ничего умнее на тот момент не придумал..
            if (params.get(key) instanceof Long) {
                Long val = (Long) params.get(key);
                return val.intValue();
            }
            return params.get(key);
        } else {
            return null;
        }
    }
}
