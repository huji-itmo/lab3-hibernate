import databaseClasses.Emotion;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.BasicType;

import javax.persistence.criteria.CriteriaQuery;
import java.util.EmptyStackException;
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
                    System.out.println("New emotion was added! Type \"show_emotions\" to see.");
                }

                lastCount = count;
            }
        }, intervalInSeconds* 1000L, intervalInSeconds* 1000L);
    }

    public static int getCount(SessionFactory factory) {
        try (Session session = factory.openSession()) {
            CriteriaQuery<Emotion> query=  session.getCriteriaBuilder().createQuery(Emotion.class);

            query.select(query.from(Emotion.class));

            return session.createQuery(query).getResultList().size();
        }
    }
}
