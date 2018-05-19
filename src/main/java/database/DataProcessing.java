package database;

import data_processing.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DataProcessing {

    // TODO: нужно рефакторить!
    public static void insertPrimaryUsers(JSONObject params, Connection con) {
        try {
            PreparedStatement st = con.prepareStatement("INSERT INTO handshake_theory.PRIMARY_USERS (USER_ID, FIRST_NAME, LAST_NAME," +
                    "SEX, IS_FRIEND, COMMON_COUNT, HOME_TOWN, IS_VERIFIED, SCAN_DATE, BDATE, LATITUDE, LONGITUDE," +
                    "LOCATION, RADIUS, DESCRIPTION, FOLLOWERS_COUNT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            st.setLong(1, (Long) Utils.checkExist(params.get("id")));
            st.setString(2, (String) Utils.checkExist(params.get("first_name")));
            st.setString(3, (String) Utils.checkExist(params.get("last_name")));
            st.setInt(4, (int) (long) Utils.checkExist(params.get("sex")));
            st.setInt(5, (int) (long) Utils.checkExist(params.get("is_friend")));
            st.setInt(6, (int) (long) Utils.checkExist(params.get("common_count")));
            st.setString(7, (String) Utils.checkExist(params.get("home_town")));
            st.setInt(8, (int) (long) Utils.checkExist(params.get("verified")));
            st.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            st.setString(10, (String) Utils.checkExist(params.get("bdate")));
            st.setFloat(11, (float) Utils.checkExist(params.get("latitude")));
            st.setFloat(12, (float) Utils.checkExist(params.get("longitude")));
            st.setString(13, (String) Utils.checkExist(params.get("location")));
            st.setInt(14, (int) Utils.checkExist(params.get("radius")));
            JSONArray array = (JSONArray) params.get("descriptions");
            st.setString(15, (String) Utils.checkExist(array.get(0)));
            st.setInt(16, (int) (long) Utils.checkExist(params.get("followers_count")));
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static boolean filterDuplicates(JSONObject params, Connection con) {
//        try {
//            PreparedStatement st = con.prepareStatement("SELECT * FROM handshake_theory.PRIMARY_USERS WHERE USER_ID = ?");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
