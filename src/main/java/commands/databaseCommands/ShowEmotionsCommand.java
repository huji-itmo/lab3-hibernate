package commands.databaseCommands;

import commands.exceptions.CommandException;
import commands.nativeCommands.Command;
import databaseClasses.Conversation;
import databaseClasses.Emotion;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowEmotionsCommand implements Command {
    private final SessionFactory factory;
    @Override
    public String execute(String args) throws CommandException {
        Session session = factory.openSession();
        session.beginTransaction();

        List<Emotion> list = session.createNativeQuery("select * from emotions",
                Emotion.class).list();
        String res = list.stream().map(Objects::toString).map(str -> str + "\n").collect(Collectors.joining());

        session.getTransaction().commit();
        session.close();

        return res;
    }

    @Override
    public String getDescription() {
        return "Shows emotions";
    }
}
