package myImplement;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DbUtil {
    private static String className = "com.mysql.cj.jdbc.Driver";
    private static String dataUser = "root";
    private static String dataUrl =
            "jdbc:mysql://127.0.0.1/szureptile?useSSL=false&user=" + dataUser + "&password=";
    private static Connection conn = null;


    public static Connection getConn() {
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(dataUrl);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Set<String> getUrl() {
        Set<String> urlSet = new HashSet<>(50);
        Connection connection = DbUtil.getConn();
        String sql = "select url from record order by id Desc limit 50";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                urlSet.add(resultSet.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urlSet;
    }



    public static void insert(Set<String> newUrlSet) {
        Connection connection;
        try {
            connection = DbUtil.getConn();
            for (String url : newUrlSet) {
                String sql = "insert into record (url) values(?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, url);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
