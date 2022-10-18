public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places){
        if(places == null) return 0;

        int BestScore = 0;
        int TempScore;
        final int ARRAY_SIZE = places.length;
        for(int i = 0; i < ARRAY_SIZE ; ++i){
            for(int j = i+1; j < ARRAY_SIZE ; ++j){
                TempScore = (places[i] + places[j] + (i - j));
                if(TempScore>BestScore) BestScore = TempScore;
            }
        }

        return BestScore;
    }

    public static void main(String[] args) {
        System.out.println(getBestSightseeingPairScore(new int[]{8, 1, 5, 2, 6}));
        System.out.println(getBestSightseeingPairScore(new int[]{1, 2}));
    }
}
