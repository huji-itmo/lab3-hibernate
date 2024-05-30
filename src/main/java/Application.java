import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.databaseCommands.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Optional;

public class Application {
    public static void run(String[] args) {
        if (args.length != 1) {
            System.out.println("Pass .pgpass file path!");
            System.exit(0);
        }

        SessionFactory factory = null;

        try {

            factory = HibernateSessionFactory.setupFactory(args[0]);

            Session session = factory.openSession();
            session.beginTransaction();

            TriggerQueryCreator.createAndExecuteNativeSQLTrigger(HibernateSessionFactory.config, session);

            session.getTransaction().commit();
            session.close();

        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        
        setupChecker(factory);

        AbstractCommandProcessor commandProcessor = new CommandProcessor();

        commandProcessor.getCommands().put("show_conversations", new ShowConversationsCommand(factory));
        commandProcessor.getCommands().put("show_people", new ShowPeopleCommand(factory));
        commandProcessor.getCommands().put("show_robots", new ShowRobotsCommand(factory));
        commandProcessor.getCommands().put("show_emotions", new ShowEmotionsCommand(factory));

        commandProcessor.getCommands().put("add_conversation", new AddConversationCommand(factory));
        commandProcessor.getCommands().put("add_person", new AddPersonCommand(factory));
        commandProcessor.getCommands().put("add_emotion", new AddEmotionCommand(factory));
        commandProcessor.getCommands().put("add_robot", new AddRobotCommand(factory));

        commandProcessor.getCommands().put("execute", new ExecCommand(factory));

        System.out.println("Start typing your requests!");
        new ConsoleProcessor(commandProcessor).startLoop();
    }


    static NewEmotionChecker checker = null;
    private static void setupChecker(SessionFactory factory) {
        int interval = Optional.of(HibernateSessionFactory.config.getProperty("lab3.new_emotion.check_interval_in_seconds")).map(Integer::parseInt).orElse(-1);

        if (interval <= 0) {
            return;
        }

        checker = new NewEmotionChecker(factory, interval);

    }
}
