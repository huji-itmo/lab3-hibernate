package databaseClasses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "robots")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "robots_id_seq")
    @SequenceGenerator(name="robots_id_seq", sequenceName="robots_id_seq",allocationSize=1)
    private long id;

    private String model;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Conversation.class)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private double brevity;
    private double damage;

    public static Robot createFromInput(Scanner scanner, SessionFactory factory) {
        return Robot.builder()
                .model(enterModel(scanner))
                .conversation(Person.chooseConversation(scanner,factory))
                .brevity(enterBrevity(scanner))
                .damage(enterDamage(scanner))
                .build();
    }

    private static double enterDamage(Scanner scanner) {
        System.out.println("Enter damage");
        try {
            return Double.parseDouble(scanner.nextLine());
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return enterDamage(scanner);

        }
    }

    private static double enterBrevity(Scanner scanner) {
        System.out.println("Enter brevity");
        try {
            double in = Double.parseDouble(scanner.nextLine());
            if (in < 0 || in > 10) {
                System.err.println("Brevity can't be negative!");
                enterBrevity(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return enterBrevity(scanner);

        }

    }

    private static String enterModel(Scanner scanner) {
        System.out.println("Enter model");
        try {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                System.err.println("Model can't be null!");
                enterModel(scanner);
            }

            return line;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return enterModel(scanner);
        }
    }
}
