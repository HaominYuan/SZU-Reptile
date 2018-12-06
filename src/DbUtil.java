import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class DbUtil {
    private static String driver;
    private static String user;
    private static String password;
    private static String url;
    private static Connection conn = null;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("Db.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        user = properties.getProperty("user");
        url = properties.getProperty("url");
        password = properties.getProperty("password");
    }


    private static Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url + "&user=" + user + "&password=" + password);

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
