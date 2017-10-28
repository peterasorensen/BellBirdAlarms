import org.junit.Test;
import org.junit.Ignore;

import java.sql.Connection;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Aydin on 10/28/2017.
 */
public class UtilitiesUnitTest {
    @Test
    public void testCreateTable() throws ParseException {
        Utilities.createTable();
        assertEquals(1, 1);
    }

    @Test
    public void testPrintMessage() throws ParseException {
        // Utilities.createTable();
        // Utilities.createAlarm("newAlarm2", "10/28/2017 01:01:00");
        String returned = Utilities.viewAlarms();
        System.out.println(returned);
        assertEquals("Alarm1:ID = newAlarm2, DateCreated = 10/28/2017 00:49:50, DateAlarm = 10/28/2017 01:01:00, Upvotes = 0", returned);
    }
}
