import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.databaseCommands.AddClassCommand;
import commands.databaseCommands.ExecCommand;
import commands.databaseCommands.ShowClassCommand;
import databaseClasses.Conversation;
import databaseClasses.Emotion;
import databaseClasses.Person;
import databaseClasses.Robot;
import org.hibernate.SessionFactory;

import java.util.Scanner;

public class CommandProcessorMapFill {

    public static void fill(AbstractCommandProcessor commandProcessor, SessionFactory factory) {
        commandProcessor.getCommands().put("show_conversations", new ShowClassCommand<>(factory, Conversation.class));
        commandProcessor.getCommands().put("show_people", new ShowClassCommand<>(factory, Person.class));
        commandProcessor.getCommands().put("show_robots", new ShowClassCommand<>(factory, Robot.class));
        commandProcessor.getCommands().put("show_emotions", new ShowClassCommand<>(factory, Emotion.class));

        commandProcessor.getCommands().put("add_conversation", new AddClassCommand(factory,
                () -> Conversation.createFromInput(new Scanner(System.in))));
        commandProcessor.getCommands().put("add_person", new AddClassCommand(factory,
                () -> Person.createFromInput(new Scanner(System.in), factory)));
        commandProcessor.getCommands().put("add_emotion", new AddClassCommand(factory,
                () -> Emotion.createFromInput(new Scanner(System.in), factory)));
        commandProcessor.getCommands().put("add_robot", new AddClassCommand(factory,
                () -> Robot.createFromInput(new Scanner(System.in), factory)));
        commandProcessor.getCommands().put("execute", new ExecCommand(factory));

    }
}
