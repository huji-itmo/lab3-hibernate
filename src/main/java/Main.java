import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.databaseCommands.*;
import commands.nativeCommands.ExecuteScriptCommand;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Application.run(args);
    }
}
