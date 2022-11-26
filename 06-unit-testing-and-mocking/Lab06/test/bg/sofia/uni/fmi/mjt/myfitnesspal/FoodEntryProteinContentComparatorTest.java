package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntry;
import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntryProteinContentComparator;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodEntryProteinContentComparatorTest {

    private static final FoodEntryProteinContentComparator COMP = new FoodEntryProteinContentComparator();
    private static final NutritionInfo DEFAULT_NUTRITION_INFO = new NutritionInfo(70,29,1);
    private static final String DEFAULT_NAME = "Rice";
    private static final double DEFAULT_SERVING = 2;

    @Test
    void testCompareWithSameFoodEntry() {
        FoodEntry o1 = new FoodEntry(DEFAULT_NAME, DEFAULT_SERVING, DEFAULT_NUTRITION_INFO);
        assertEquals(COMP.compare(o1,o1),0,"Comparator should return 0 for Equal food entries");
    }

    @Test
    void testCompareWithObjectOneContentBiggerThanObjectTwo() {
        FoodEntry o1 = new FoodEntry(DEFAULT_NAME, DEFAULT_SERVING, DEFAULT_NUTRITION_INFO);
        FoodEntry o2 = new FoodEntry(DEFAULT_NAME,DEFAULT_SERVING - 1, DEFAULT_NUTRITION_INFO);
        assertTrue(isBigger(COMP.compare(o1,o2)),"Comparator should return 0 for Equal food entries");
    }
    @Test
    void testCompareWithObjectTwoContentBiggerThanObjectOne() {
        FoodEntry o1 = new FoodEntry(DEFAULT_NAME,DEFAULT_SERVING, DEFAULT_NUTRITION_INFO);
        FoodEntry o2 = new FoodEntry(DEFAULT_NAME,DEFAULT_SERVING - 1, DEFAULT_NUTRITION_INFO);
        assertFalse(isBigger(COMP.compare(o2,o1)),"Comparator should return 0 for Equal food entries");
    }

    private boolean isBigger(int o1) {
        return o1 > 0;
    }

}
