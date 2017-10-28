import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aydin on 10/27/2017.
 */
public class Alarm {
    private String alarm_id;
    private Date dateCreated;
    private Date dateAlarm;
    private int upvotes;
    public Alarm(String alarm_id, String alarmTime) throws ParseException {
        this.alarm_id = alarm_id.toUpperCase();
        this.dateCreated = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = dateFormat.parse(alarmTime);
            this.dateAlarm = date;
        } catch (ParseException e) {
            throw new ParseException("Date was formatted badly!", 0);
        }
        this.upvotes = 0;
    }

    public void upvote() {
        this.upvotes++;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateAlarm() {
        return dateAlarm;
    }

    public int getUpvotes() {
        return upvotes;
    }
}
