package database;

import data_processing.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class DataProcessing {

    // TODO: нужно рефакторить!
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
     * Проверка наличия пользователя в БД.
     * @return true - если запись с подобным USER_ID (VK) уже есть, false - если записи нет.
     * @throws SQLException
     */
    public static boolean hasDuplicates(JSONObject params, Connection con) throws SQLException {
        boolean result;
        PreparedStatement st = con.prepareStatement("SELECT * FROM handshake_theory.PRIMARY_USERS WHERE USER_ID = ?");
        st.setLong(1, (Long) params.get("id"));
        st.execute();
        ResultSet rs = st.getResultSet();

        if (rs.next()) {
            result = true;
        } else {
            result = false;
        }
        rs.close();
        st.close();
        return result;

    }
}
