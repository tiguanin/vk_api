package db.database;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataProcessing {

    /**
     * Получение списка с PRIMARY_USERS.USER_ID по жестко заданным условиям.
     * @return
     */
    public static List<String> getUserIdList(Connection con) {
        List<String> ids = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT user_id FROM handshake_theory.primary_users;";
        try {
            st = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.execute();
            rs = st.getResultSet();

            if (rs.first()) {
                do {
                    ids.add(rs.getString("user_id"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(st);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(con);
        }
        return ids;

    }



}
