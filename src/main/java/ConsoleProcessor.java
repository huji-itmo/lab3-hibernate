import commands.AbstractCommandProcessor;
import commands.CommandProcessor;
import commands.exceptions.CommandException;
import lombok.AllArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Scanner;

@AllArgsConstructor
public class ConsoleProcessor {

    private final AbstractCommandProcessor processor;
    public void startLoop() {

        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                try {
                    processor.executeCommand(scanner.nextLine());
                } catch (CommandException e) {
                    System.err.println(e.getMessage());
                } catch(Throwable e) {
                    System.err.print(e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Can't read string...");
            System.exit(0);
        }
    }
}
