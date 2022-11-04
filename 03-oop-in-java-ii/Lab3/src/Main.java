import bg.sofia.uni.fmi.mjt.escaperoom.room.Difficulty;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Theme;

public class Main {

    public static void main(String[] args) {
        //(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
        //                      int maxReviewsCount)
        EscapeRoom escapeRoom = new EscapeRoom("Escape", Theme.FANTASY, Difficulty.HARD, 3, 3, 4);

        escapeRoom.addReview(new Review(3, "one"));
        escapeRoom.addReview(new Review(3, "two"));
        escapeRoom.addReview(new Review(3, "three"));
        escapeRoom.addReview(new Review(3, "Four"));
        escapeRoom.addReview(new Review(3, "Five"));
        escapeRoom.addReview(new Review(3, "Six"));
        escapeRoom.addReview(new Review(3, "Seven"));

        Review[] RESULT = escapeRoom.getReviews();
        for (Review A : RESULT) {
            System.out.println(A.reviewText());
        }

    }
}
