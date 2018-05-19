package logic;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import constants.AppInformation;
import objects.Place;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.DBTools.configureDBConnection;
import static database.DataProcessing.insertPrimaryUsers;
import static logic.RequestTools.executeCustomNearbyRequest;

public class StartupApp {

    static Connection con = configureDBConnection();

    public static void main(String args[]) throws IOException, ParseException, ApiException, ClientException, InterruptedException {
        //TODO: переделать на enum.
        Place temirgoevskaya = new Place("Ст. Темиргоевская", 45.114117F, 40.287209F);
        Place moscowKremlin = new Place("Красная площадь", 55.754868F, 37.620578F);
        Place krasnodar = new Place("г. Краснодар", 45.030014F, 38.979964F);
        Place spb = new Place("Санкт-Петербург", 59.929216F, 30.318146F);
        List<Long> idList = new ArrayList<>();

        try {
            JSONObject obj = executeCustomNearbyRequest(AppInformation.TOKEN, spb, 4);
            System.out.println(obj.toString());

            JSONArray array = (JSONArray) obj.get("items");
            for (int i = 0; i < array.size(); i++) {
                JSONObject params = (JSONObject) array.get(i);
                insertPrimaryUsers(params, con);
            }
//            idList = parsePrimaryUsers(obj);
//            executeCustomUserGetInfoRequest(idList);

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}