package db.database.inserts;

import db.data_processing.Utils;
import org.apache.commons.dbutils.DbUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static db.database.checking.CheckingData.hasDuplicates;

public class InsertData {

    /**
     * Вставка записей в таблицу PRIMARY_USERS.
     */
    public static void insertPrimaryUsers(JSONObject params, Connection con) {
        try {
            if (!hasDuplicates(params, con)) {
                PreparedStatement st = con.prepareStatement("INSERT INTO handshake_theory.PRIMARY_USERS (USER_ID, FIRST_NAME, LAST_NAME," +
                        "SEX, IS_FRIEND, COMMON_COUNT, HOME_TOWN, IS_VERIFIED, SCAN_DATE, BDATE, LATITUDE, LONGITUDE," +
                        "LOCATION, RADIUS, DESCRIPTION, FOLLOWERS_COUNT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                st.setObject(1, (Integer) Utils.checkExist("id", params));
                st.setString(2, (String) Utils.checkExist("first_name", params));
                st.setString(3, (String) Utils.checkExist("last_name", params));
                st.setObject(4, (Integer) Utils.checkExist("sex", params));
                st.setObject(5, (Integer) Utils.checkExist("is_friend", params));
                st.setObject(6, (Integer) Utils.checkExist("common_count", params));
                st.setString(7, (String) Utils.checkExist("home_town", params));
                st.setObject(8, (Integer) Utils.checkExist("verified", params));
                st.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                st.setString(10, (String) Utils.checkExist("bdate", params));
                st.setFloat(11, (Float) Utils.checkExist("latitude", params));
                st.setFloat(12, (Float) Utils.checkExist("longitude", params));
                st.setString(13, (String) Utils.checkExist("location", params));
                st.setObject(14, (Integer) Utils.checkExist("radius", params));
                JSONArray array = (JSONArray) params.get("descriptions");
                st.setString(15, (String) array.get(0));
                st.setObject(16, (Integer) Utils.checkExist("followers_count", params));
                st.executeUpdate();
                st.close();
            } else {
                System.out.println("Запись с user_id = " + params.get("id") + " уже существует. Вставка не производится.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Вставка записей в таблицу PRIMARY_USERS_FRIENDS.
     */
    public static void insertPrimaryUsersFriends(Connection con, int primaryUserId, int friendId) {
        String sql = "INSERT INTO handshake_theory.primary_users_friends_list (primary_user_id, friend_user_id, scan_date) VALUES (?, ?, ?);";
        PreparedStatement st = null;
        try {
            st = con.prepareStatement(sql);
            st.setObject(1, primaryUserId);
            st.setObject(2, friendId);
            st.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(st);
        }
    }

}
