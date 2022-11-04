package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class EscapeRoom implements Ratable {

    private final int maxTimeToEscape;
    private final int maxReviewsCount;
    private final double priceToPlay;
    private final Difficulty difficulty;
    private final Theme theme;
    private final String name;

    private int review_insert_index;
    private Review[] reviews;

    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount) {
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;
        this.reviews = new Review[0];
    }

    @Override
    public double getRating() {
        if (review_insert_index == 0) {
            return 0d;
        }

        double result = 0d;


        for (Review a : reviews) {
            result += a.rating();
        }

        return result / (double) (review_insert_index);
    }

    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
        return reviews;
    }

    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        if (review_insert_index >= maxReviewsCount) {
            //LOGIC Crashes here
            Review[] resultArr = new Review[maxReviewsCount];
            System.arraycopy(reviews, 1, resultArr, 0, maxReviewsCount - 1);
            resultArr[maxReviewsCount - 1] = review;
            reviews = resultArr;
            review_insert_index++;
            return;
        }

        Review[] resultRev = new Review[++review_insert_index];
        System.arraycopy(reviews, 0, resultRev, 0, review_insert_index - 1);
        resultRev[review_insert_index - 1] = review;
        reviews = resultRev;
    }

    public Theme getTheme() {
        return theme;
    }

    public double getPriceToPlay() {
        return priceToPlay;
    }
}
