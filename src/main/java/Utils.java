import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static LocalDateTime fromStringToLocalDateTime (String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
