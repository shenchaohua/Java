import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CLoseWait {
    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?user=root&password=123");
        dataSource.setMaxActive(1);
    }

    public static void sample() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from test.test");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
        connection.close();
    }
    public static void main(String[] args) throws IOException, SQLException {
        ServerSocket socket = new ServerSocket(8080);
        while (true) {
            Socket accept = socket.accept();
            sample();
            accept.close();
        }
    }
}
