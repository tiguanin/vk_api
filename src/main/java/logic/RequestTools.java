package logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.responses.GetNearbyResponse;
import com.vk.api.sdk.queries.users.UsersGetNearbyRadius;
import constants.AppInformation;
import db.database.inserts.InsertData;
import objects.Place;
import org.apache.commons.dbutils.DbUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static logic.Tools.configureUserGetInfoURL;

public class RequestTools {

    public static void locationRequestWrapper(Place place, UsersGetNearbyRadius areaRadius) throws ParseException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        try {
            UserActor actor = new UserActor(AppInformation.APP_ID, AppInformation.TOKEN);
            GetNearbyResponse response = vk.users()
                    .getNearby(actor, place.getLatitude(), place.getLongitude())
                    .radius(areaRadius)
                    .execute();

            String result = response.toString();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(result);
            String res = (String) jsonObject.get("items");
            System.out.println(res);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

    }


    /**
     * Получение данных о пользователях, находящихся рядом с заданной локацией.
     *
     * @param token  - токен пользователя
     * @param place  - см. класс Place
     * @param radius - радиус площади поиска (по дефолту 1)
     */
    // TODO: использую самопалюную версию, т.к. в API баг (метод getNearby возвращает конское кол-во параметров).
    public static JSONObject executeCustomNearbyRequest(String token, Place place, int radius) throws IOException, ParseException {
        String url = "https://api.vk.com/method/users.getNearby?access_token=" + token + "&radius=" + radius + "&need_description=1&v=5.74";
        url = Tools.configureLocationURL(place, url, true);
        System.out.println(url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            String respStr = EntityUtils.toString(response.getEntity());
            System.out.println(respStr);

            JSONParser parser = new JSONParser();
            JSONObject result = (JSONObject) parser.parse(respStr);
            // отсекаю лишнюю информацию о родительском объекте (читаю только содержимое объекта response)
            result = (JSONObject) result.get("response");
            result = Tools.addMetaData(result, place, radius);
            return result;
        } finally {
            response.close();
        }
    }

    public static List<JSONObject> executeCustomUserGetInfoRequest(List<Long> idList) throws InterruptedException, UnsupportedEncodingException {
        String url = "https://api.vk.com/method/users.get?access_token=" + AppInformation.TOKEN + "&v=5.74";
        url = configureUserGetInfoURL(url);
        List<JSONObject> objectCollection = new ArrayList<>();
        for (Long id : idList) {
            JSONObject temp = executeGetRequest(url + "&user_ids=" + id);
            objectCollection.add(temp);
            Thread.sleep(200);
        }
        for (JSONObject obj : objectCollection) {
            System.out.println(obj.toString());
        }
        return objectCollection;

    }

    /**
     * GET-запрос по указанному URL.
     *
     * @param url - URL, который нужно послать серверу методом GET
     * @return JSONObject с ответом сервера
     */
    public static JSONObject executeGetRequest(String url) {
        JSONObject result = null;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            System.out.println(response.getStatusLine());
            String responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            try {
                JSONParser parser = new JSONParser();
                result = (JSONObject) parser.parse(responseString);
                httpClient.close();
                response.close();
                return result;
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Произошли исключения на стадии парсинга json-a, возвращаю NULL!");
        return result;
    }


    public static void getUserInfo(List<String> ids, Connection con) {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        UserActor userActor = new UserActor(AppInformation.APP_ID, AppInformation.TOKEN);
        int friendsCount = 0;
        int responseCount = 0;
        try {
//          UsersGetQuery usersGetQuery = new UsersGetQuery(vk, userActor);
//          System.out.println(usersGetQuery.fields().userIds(ids).execute());

            for (String id : ids) {
                JsonElement element = vk.execute().batch(userActor, vk.friends().get(userActor).userId(Integer.parseInt(id))).execute();
                JsonArray arr = element.getAsJsonArray();
                if (arr.get(0).toString().equals("false")) {
                    System.out.println("В ответ прилетел false!");
                    continue;
                }
                JsonObject obj = (JsonObject) arr.get(0);
                arr = (JsonArray) obj.get("items");
                for (int i = 0; i < arr.size(); i++) {
                    InsertData.insertPrimaryUsersFriends(con, Integer.parseInt(id), Integer.parseInt(arr.get(i).toString()));
                    friendsCount++;
                }

                System.out.println(responseCount + " " + element.toString());
                responseCount++;
                Thread.sleep(388);
            }

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Суммарное количество друзей всех выбранных 999 пользователей: " + friendsCount);
            DbUtils.closeQuietly(con);
        }

    }
}


