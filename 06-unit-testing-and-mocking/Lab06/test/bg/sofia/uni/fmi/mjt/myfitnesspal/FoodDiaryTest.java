package bg.sofia.uni.fmi.mjt.myfitnesspal;

import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.DailyFoodDiary;
import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.FoodEntry;
import bg.sofia.uni.fmi.mjt.myfitnesspal.diary.Meal;
import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FoodDiaryTest {

    private static final String DEFAULT_FOOD_NAME = "Rice";
    private static final double DEFAULT_SERVING_SIZE = 2;
    private static final NutritionInfo DEFAULT_NUTRITION_INFO = new NutritionInfo(70,29,1);
    private static final FoodEntry DEFAULT_FOOD_ENTRY = new FoodEntry(DEFAULT_FOOD_NAME,DEFAULT_SERVING_SIZE,DEFAULT_NUTRITION_INFO);
    private static final Meal DEFAULT_MEAL = Meal.DINNER;

    @Mock
    private NutritionInfoAPI nutritionInfoAPIMock;

    @InjectMocks
    private DailyFoodDiary foodDiary;

    @Test
    void testAddFoodWithNullMeal() {
       assertThrows(IllegalArgumentException.class,() -> foodDiary.addFood(null,DEFAULT_FOOD_NAME,DEFAULT_SERVING_SIZE)
               ,"Adding Null meal should throw an IllegalArgumentException");
    }
    @Test
    void testAddFoodWithNullName() {
        assertThrows(IllegalArgumentException.class,() -> foodDiary.addFood(DEFAULT_MEAL,null,DEFAULT_SERVING_SIZE));
    }
    @Test
    void testAddFoodWithNegativeServingSize(){
        assertThrows(IllegalArgumentException.class,() -> foodDiary.addFood(DEFAULT_MEAL,DEFAULT_FOOD_NAME,-12));
    }

    @Test
    void testAddFoodWithNutritionInfoAvailable() throws UnknownFoodException {
        when(nutritionInfoAPIMock.getNutritionInfo(DEFAULT_FOOD_NAME)).thenReturn(DEFAULT_NUTRITION_INFO);

        FoodEntry def = foodDiary.addFood(DEFAULT_MEAL,DEFAULT_FOOD_NAME,DEFAULT_SERVING_SIZE);

        assertEquals(DEFAULT_FOOD_ENTRY,def,"Add food method Returned a non matching FoodEntry");
    }
    @Test
    void testAddFoodWithNutritionInfoUnavailable() throws UnknownFoodException {
        when(nutritionInfoAPIMock.getNutritionInfo(DEFAULT_FOOD_NAME))
                .thenThrow(new UnknownFoodException("Test exception: No nutrition info found for " + DEFAULT_FOOD_NAME));

        assertThrows(UnknownFoodException.class,() -> foodDiary.addFood(DEFAULT_MEAL,DEFAULT_FOOD_NAME,DEFAULT_SERVING_SIZE)
                ,"Foods with no nutrition info found should throw UnknownFoodException");
    }

    @Test
    void testGetAllFoodEntriesReturnCollectionModifiability() {
        assertThrows(UnsupportedOperationException.class, () -> foodDiary.getAllFoodEntries().add(DEFAULT_FOOD_ENTRY)
                ,"GetAllFoodEntries method should return an unmodifiable collection");
    }

    @Test
    void testGetAllFoodEntriesByProteinContentModifiability() {
        assertThrows(UnsupportedOperationException.class, () -> foodDiary.getAllFoodEntriesByProteinContent().add(DEFAULT_FOOD_ENTRY)
                ,"getAllFoodEntriesByProteinContentModifiability method should return an unmodifiable collection");
    }

    @Test
    void testGetAllFoodEntriesByProteinContentSort() throws UnknownFoodException {
        when(nutritionInfoAPIMock.getNutritionInfo("Beans")).thenReturn(DEFAULT_NUTRITION_INFO);
        when(nutritionInfoAPIMock.getNutritionInfo("Mr Beans")).thenReturn(DEFAULT_NUTRITION_INFO);
        foodDiary.addFood(DEFAULT_MEAL,"Beans",DEFAULT_SERVING_SIZE + 1);
        foodDiary.addFood(DEFAULT_MEAL,"Mr Beans",DEFAULT_SERVING_SIZE);

        List<FoodEntry> foodList = foodDiary.getAllFoodEntriesByProteinContent();

        assertFalse(foodList.get(0).nutritionInfo().proteins() * foodList.get(0).servingSize() >
                foodList.get(1).nutritionInfo().proteins() * foodList.get(1).servingSize());
    }

    @Test
    void testGetDailyCaloriesIntakeOnEmptyFoods() {
        DailyFoodDiary testDiary = new DailyFoodDiary(null);
        assertEquals(0.0,testDiary.getDailyCaloriesIntake(),"Expected 0.0 for empty API");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealWithNullMeal() {
        assertThrows(IllegalArgumentException.class, () -> foodDiary.getDailyCaloriesIntakePerMeal(null)
                ,"getDailyCaloriesIntakePerMeal should throw IllegalArgumentException for null Meal");
    }

    @Test
    void testGetDailyCaloriesIntakePerMealForEmptyFoods() {
        DailyFoodDiary testDiary = new DailyFoodDiary(null);

        assertEquals(0.0,testDiary.getDailyCaloriesIntakePerMeal(Meal.LUNCH),
                "getDailyCaloriesPerMeal(LUNCH) should be 0.0 for empty food storage");
        assertEquals(0.0,testDiary.getDailyCaloriesIntakePerMeal(Meal.DINNER),
                "getDailyCaloriesPerMeal(DINNER) should be 0.0 for empty food storage");
        assertEquals(0.0,testDiary.getDailyCaloriesIntakePerMeal(Meal.BREAKFAST),
                "getDailyCaloriesPerMeal(BREAKFAST) should be 0.0 for empty food storage");
        assertEquals(0.0,testDiary.getDailyCaloriesIntakePerMeal(Meal.SNACKS),
                "getDailyCaloriesPerMeal(SNACKS) should be 0.0 for empty food storage");
    }

}
