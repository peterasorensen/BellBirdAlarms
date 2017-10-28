import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Aydin on 10/27/2017.
 */
public class Utilities {
    /**
     * Please put alarmTime in format: MM/dd/yyyy HH:mm:ss
     * @param alarm_id
     * @param alarmTime
     */
    public static void createAlarm(String alarm_id, String alarmTime) throws ParseException {
        Alarm created = new Alarm(alarm_id, alarmTime);
        Connection c = connectToDB();
        Statement stmt = null;
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO ALARMS (ID,DATECREATED,DATEALARM,UPVOTES) " +
                    "VALUES ('" + alarm_id + "', " + created.getDateCreated().getTime() +
                    ", " + created.getDateAlarm().getTime() + ", " + created.getUpvotes() + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
        sendPost(created);
    }

    public static String viewAlarms() {
        Connection c = connectToDB();
        Statement stmt = null;
        String toReturn = "";
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ALARMS ORDER BY DATECREATED DESC;" );
            int alarmNumber = 1;
            while ( rs.next() ) {
                String id = rs.getString("ID");
                long dateCreated = rs.getLong("DATECREATED");
                long dateAlarm = rs.getLong("DATEALARM");
                int upvotes = rs.getInt("UPVOTES");

                System.out.print("Alarm" + alarmNumber + ":ID = " + id);
                Date date = new Date(dateCreated);
                Date date2 = new Date(dateAlarm);
                SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String dateCreatedText = df2.format(date);
                String dateAlarmText = df2.format(date2);
                System.out.print(", DateCreated = " + dateCreatedText);
                System.out.print(", DateAlarm = " + dateAlarmText);
                System.out.print(", Upvotes = " + upvotes);
                System.out.println();
                alarmNumber++;
                toReturn += "Alarm" + alarmNumber + ":ID = " + id + ", DateCreated = " + dateCreatedText +
                        ", DateAlarm = " + dateAlarmText + ", Upvotes = " + upvotes;
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return toReturn;
    }

    private static Connection connectToDB() {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:bellbird");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;
    }

    public static void createTable() {
        Connection c = connectToDB();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
//            String sql = "DROP TABLE ALARMS;";
            String sql = "CREATE TABLE ALARMS " +
                    "(ID TEXT PRIMARY KEY     NOT NULL," +
                    " DATECREATED    LONG     NOT NULL, " +
                    " DATEALARM      LONG     NOT NULL, " +
                    " UPVOTES        INT      NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    public static void upvote(String alarm_id) {
        Connection c = connectToDB();
        Statement stmt = null;
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE ALARMS set UPVOTES = UPVOTES + 1 where ID='" + alarm_id + "';";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    private static void sendPost(Alarm alarm) {
        try {
            URL url = new URL("http://handshake-bellbird.herokuapp.com/push");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            Map<String,String> arguments = new HashMap<>();
            arguments.put("alarm_id", alarm.getAlarm_id());
            arguments.put("dateCreated", alarm.getDateCreated().toString());
            arguments.put("dateAlarm", alarm.getDateAlarm().toString());
            arguments.put("upvotes", Integer.toString(alarm.getUpvotes()));
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));

            byte[] out = "{\"alarm_id\":\"dateCreated\",\"dateAlarm\":\"upvotes\"}" .getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException {
        // Two lines below will create DB and Alarms Table
//        Connection c = connectToDB();
//        createTable(c);

        // Line below will create an Alarm at specified date
//        createAlarm("newAlarm2", "10/28/2017 01:01:00");

        // Line below will list out alarms in DateCreated order
        viewAlarms();

        // Line below will add one Upvote to an alarm
//        upvote("newAlarm2");
    }
}
