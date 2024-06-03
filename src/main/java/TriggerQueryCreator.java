import databaseClasses.Conversation;
import databaseClasses.Emotion;
import databaseClasses.Person;
import databaseClasses.Robot;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.TypeHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.internal.expression.function.FunctionExpression;
import org.hibernate.type.FloatType;
import org.hibernate.type.NumericBooleanType;
import org.hibernate.type.Type;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Setter(AccessLevel.PRIVATE)
public class TriggerQueryCreator {
    public static final String TRIGGER_PATH_PROPERTY = "lab3.new_emotion.trigger_path_from_resources";

    public static final String prefix =  "lab3.new_emotion.";
    public static final List<String> PARAMETERS = List.of(
            "NEW_DESPAIR_VALUE",
            "NEW_JOY_VALUE",
            "NEW_FEAR_VALUE",
            "BREVITY_THRESH_HOLD");

    public static void createAndExecuteNativeSQLTrigger(Configuration configuration, Session session) throws IOException {
        String path = configuration.getProperty(TRIGGER_PATH_PROPERTY);

        NativeQuery query = session.createSQLQuery(loadStringFromResources(path));
        query.executeUpdate(); //returns null
    }

    public static String loadStringFromResources(String path) throws IOException {
        InputStream stream = Optional.ofNullable(TriggerQueryCreator.class.getResourceAsStream(path))
                .orElseThrow(() -> new FileNotFoundException("Cant find resource: " + path));

        try (BufferedInputStream inputStream = new BufferedInputStream(stream)) {
            return new String(inputStream.readAllBytes());
        }
    }
}
