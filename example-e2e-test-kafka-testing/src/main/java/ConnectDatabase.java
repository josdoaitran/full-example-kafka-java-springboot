import java.sql.*;

public class ConnectDatabase {


    public static void deleteDatabase(String url, String userName, String password, String sqlStatement) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url+ "?user=" + userName + "&password=" + password);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlStatement);

    }

    public static ResultSet checkDatabase(String url, String userName, String password, String sqlStatement) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url+ "?user=" + userName + "&password=" + password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStatement);
        System.out.println("Printing schema for table: " + resultSet.getMetaData().getTableName(1));
        int columnCount = resultSet.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            System.out.println(i + " " + resultSet.getMetaData().getColumnName(i));
        }

        System.out.println("Searching for example user.");
        boolean exampleUserFound = false;

        return resultSet;
    }
}
