package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NutritionInfoTest {

    @Test
    void testCompactConstructorWithNegativeCarbohydrates(){
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-32, 100,32)
                ,"Negative carbohydrates gram count should throw IllegalArgumentException");
    }

    @Test
    void testCompactConstructorWithNegativeFats(){
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(100, -30,30)
                ,"Negative fats gram  count should throw IllegalArgumentException");
    }

    @Test
    void testCompactConstructorWithNegativeProteins(){
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(100, 20,-20)
                ,"Negative protein gram  count should throw IllegalArgumentException");
    }

    @Test
    void testCompactConstructorWithGramsNotEqualToHundred(){
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(100, 20,34)
                ,"Gram count with sum != 100 should throw IllegalArgumentException");
    }

    @Test
    void testCaloriesOneHundredCarbs(){
        assertEquals(new NutritionInfo(100, 0,0).calories(),400
                ,"calories method calculation incorrect");
    }

    @Test
    void testCaloriesOneHundredFats(){
        assertEquals(new NutritionInfo(0, 100,0).calories(),900
                ,"calories method calculation incorrect");
    }
    @Test
    void testCaloriesOneHundredProteins(){
        assertEquals(new NutritionInfo(0, 0,100).calories(),400
                ,"calories method calculation incorrect");
    }

}
