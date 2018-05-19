package logic;

import objects.Place;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    /**
     * Формирует URL на основе заданного мета - Place.
     */
    public static String configureLocationURL(Place place, String url, boolean fullInfo) throws UnsupportedEncodingException {
        url = url + "&latitude=" + place.getLatitude() + "&longitude=" + place.getLongitude();

        if (fullInfo) {
            url = url + "&fields=is_friend";
            url = url + ",common_count";
            url = url + ",followers_count";
            url = url + ",home_town";
            url = url + ",sex";
            url = url + ",verified";
            url = url + ",bdate";
            // url = url + ",universities";
        }
        url = url + URLEncoder.encode(url, "UTF-8");
        return url;
    }

    public static String configureUserGetInfoURL(String url) throws UnsupportedEncodingException {
        url = url + "&fields=is_friend";
        url = url + ",common_count";
        url = url + ",followers_count";
        url = url + ",home_town";
        url = url + ",sex";
        url = url + ",verified";
        url = url + ",bdate";
        url = url + ",count=friends";

        url = url + URLEncoder.encode(url, "UTF-8");

        return url;
    }

    /**
     * Парсинг всех ID первичных пользователей. Нужно для нахождения их друзей.
     */
    public static List<Long> parsePrimaryUsers(JSONObject usersQuery) {
        List<Long> idList = new ArrayList<>();
        JSONArray idJsonArray = (JSONArray) usersQuery.get("items");

        for (int i = 0; i < idJsonArray.size(); i++) {
            JSONObject temp = (JSONObject) idJsonArray.get(i);
            idList.add((long) temp.get("id"));
        }
        return idList;
    }

    public static void extractUserInfoById(List<Long> idList) {
        for (Long id : idList) {

        }
    }

    /**
     * Добавление метаданных (параметры локации) для кадого пользователя.
     */
    public static JSONObject addMetaData(JSONObject object, Place place, int radius) {
        JSONArray array = (JSONArray) object.get("items");
        for (int i = 0; i < array.size(); i++) {
            JSONObject element = (JSONObject) array.get(i);
            if (element.get("is_friend") == null) {
                System.out.println("У этого чувака нет флага is_friend, id:" + element.get("id"));
            }
            element.put("location", place.getName());
            element.put("latitude", place.getLatitude());
            element.put("longitude", place.getLongitude());
            element.put("radius", radius);
        }
        object.put("items", array);
        return object;
    }
}
