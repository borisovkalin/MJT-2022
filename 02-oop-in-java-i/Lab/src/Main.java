import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {

        LocalDateTime ui = LocalDateTime.now().minusHours(2);
        LocalDateTime ui2 = LocalDateTime.now();

        ui.until(ui2, ChronoUnit.DAYS);
        System.out.println(ui.until(ui2, ChronoUnit.DAYS));
    }
}