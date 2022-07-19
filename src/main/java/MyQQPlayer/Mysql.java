package MyQQPlayer;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mysql {








    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL ="jdbc:mysql://localhost:3306/account?useUnicode=true&characterEncoding-utf8&rewriteBatchedStatement=true&useSSL=false";
    static final String USER ="root";
    static final String PASS ="123456";

    public Connection conn;

    public Statement stmt;

    public PreparedStatement pstmt;

    public ResultSet rst;

    Mysql(){
        conn = null;
        stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            //System.out.println("连接数据库....");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
