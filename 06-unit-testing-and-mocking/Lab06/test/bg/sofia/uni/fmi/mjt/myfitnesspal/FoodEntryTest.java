package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntry;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    @Test
    void testFoodEntryWithNullFoodName(){
        assertThrows(IllegalArgumentException.class , () -> new FoodEntry(null,13.5,
                        new NutritionInfo(70,30,0))
                ,"Null Food names should throw IllegalArgumentExceptions");
    }

    @Test
    void testFoodEntryWithBlankFoodName(){
        assertThrows(IllegalArgumentException.class , () -> new FoodEntry(null,13.5,
                        new NutritionInfo(70,30,0))
                ,"Blank String Food names should throw IllegalArgumentExceptions");
    }

    @Test
    void testFoodEntryWithEmptyFoodName(){
        assertThrows(IllegalArgumentException.class , () -> new FoodEntry("",13.5,
                        new NutritionInfo(70,30,0))
                ,"Empty String Food names should throw IllegalArgumentExceptions");
    }

    @Test
    void testFoodEntryWithNegativeServingSize(){
        assertThrows(IllegalArgumentException.class , () -> new FoodEntry("Rice",-2,
                        new NutritionInfo(70,30,0))
                ,"Negative serving sizes should throw IllegalArgumentExceptions");
    }

    @Test
    void testFoodEntryWithNullNutritionInfo(){
        assertThrows(IllegalArgumentException.class , () -> new FoodEntry("Pudding",13.5,null)
                ,"Null NutritionInfos should throw IllegalArgumentExceptions");
    }

    @Test
    void testFoodEntryEqualsWithDifferentServingSize() {
        assertNotEquals(new FoodEntry("Rice",2, new NutritionInfo(70,30,0)),
                new FoodEntry("Rice",3, new NutritionInfo(70,30,0))
                ," FoodEntry instances with different serving sizes should not be equal");
    }
    @Test
    void testFoodEntryEqualsWithDifferentName() {
        assertNotEquals(new FoodEntry("Rice",2, new NutritionInfo(70,30,0)),
                new FoodEntry("Rice Pudding",2, new NutritionInfo(70,30,0))
                ," FoodEntry instances with different serving sizes should not be equal");
    }
    @Test
    void testFoodEntryEqualsWithDifferentNutritionInfo() {
        assertNotEquals(new FoodEntry("Rice",2, new NutritionInfo(70,30,0)),
                new FoodEntry("Rice",2, new NutritionInfo(80,20,0))
                ," FoodEntry instances with different serving sizes should not be equal");
    }

    @Test
    void testFoodEntryEqualsWithSameServingSize() {
        assertEquals(new FoodEntry("Rice",2, new NutritionInfo(70,30,0)),
                new FoodEntry("Rice",2, new NutritionInfo(70,30,0))
                ,"Same FoodEntry instances should be equal");
    }
}
