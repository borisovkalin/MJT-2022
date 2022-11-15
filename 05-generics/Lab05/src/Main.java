import bg.sofia.uni.fmi.mjt.smartfridge.SmartFridge;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.StorableItem;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SmartFridge sf = new SmartFridge(7);
        //String name, StorableType type, LocalDate expiration)
        LocalDate yesterday = LocalDate.now();
        yesterday = yesterday.minusDays(1);
        if  (LocalDate.now().isAfter(yesterday)) {
            System.out.println("???");
        }
        Storable ni = new StorableItem("Cola", StorableType.BEVERAGE, yesterday);
        Storable ni2 = new StorableItem("Beer", StorableType.BEVERAGE, yesterday);
        Storable ni3 = new StorableItem("Beer", StorableType.BEVERAGE, LocalDate.now());
        try {
            sf.store(ni, 3);
            sf.store(ni2, 1);
            sf.store(ni, 1);
            sf.store(ni3,2);
        } catch (Exception e) {
            return;
        }

        List<? extends Storable> res = sf.removeExpired();
        for (var el : res) {
            System.out.println(el.getName());
        }

        System.out.println(sf.retrieve("Cola"));
    }
}
