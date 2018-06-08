package db.database.checking;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckingData {

    /**
     * Проверка наличия пользователя в БД.
     *
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
