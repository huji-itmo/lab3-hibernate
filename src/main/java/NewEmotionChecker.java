import databaseClasses.Emotion;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.BasicType;

import java.util.Timer;
import java.util.TimerTask;

public class NewEmotionChecker {
    Timer timer = new Timer();
    int lastCount = 0;


    public NewEmotionChecker(SessionFactory factory, int intervalInSeconds) {

        lastCount = getCount(factory);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int count = getCount(factory);

                if (count > lastCount) {
                    System.out.println("New emotion was added! Type \"show_emotions to see.\"");
                }

                lastCount = count;
            }
        }, intervalInSeconds* 1000L, intervalInSeconds* 1000L);
    }

    public static int getCount(SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();
        int count = session.createNativeQuery("select * from emotions", Emotion.class).list().size();
        session.getTransaction().commit();
        session.close();

        return count;
    }
}
