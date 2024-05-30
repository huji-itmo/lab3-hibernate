package databaseClasses;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@TypeDef(name = "enum_postgressql", typeClass = EnumTypePostgreSql.class)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversations_id_seq")
    @SequenceGenerator(name="conversations_id_seq", sequenceName="conversations_id_seq",allocationSize=1)
    private long id;

    private int length_in_seconds;

    @Enumerated(EnumType.STRING)
    @Type(type = "enum_postgressql")
    private Mood mood;


    @OneToMany(mappedBy = "id", targetEntity= Person.class, fetch = FetchType.LAZY)
    private List<Person> peopleInDialogue;

    @OneToMany(mappedBy = "id", targetEntity= Robot.class, fetch = FetchType.LAZY)
    private List<Robot> robotsInDialogue;

    public Conversation(int length_in_seconds, Mood mood) {
        this.length_in_seconds = length_in_seconds;
        this.mood = mood;
    }

    public static Conversation createFromInput(Scanner scanner) {
        return new Conversation(enterLengthInSeconds(scanner), enterMood(scanner));
    }

    private static Mood enterMood(Scanner scanner) {
        System.out.println("Enter form of education: (FormOfEducation or null)");
        System.out.println("Choose one of following: " + getEnumOptions(Mood.class));

        try {
            String line = scanner.nextLine();

            return Mood.valueOf(line);
        }
        catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return enterMood(scanner);
        }
    }


    private static int enterLengthInSeconds(Scanner scanner) {
        System.out.println("Enter length in seconds");
        try {
            int in = Integer.parseInt(scanner.nextLine());
            if (in < 0) {
                System.err.println("Length can't be negative!");
                enterLengthInSeconds(scanner);
            }

            return in;
        }
        catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        return enterLengthInSeconds(scanner);
    }

    public static String getEnumOptions(Class<? extends Enum<?>> clazz) {
        Iterator<? extends Enum<?>> iterator = Arrays.stream(clazz.getEnumConstants()).iterator();

        StringBuilder builder = new StringBuilder();

        while (iterator.hasNext()) {
            builder.append(iterator.next().name());
            if (iterator.hasNext()) {
                builder.append(" | ");
            }
        }

        return builder.toString();
    }
}
