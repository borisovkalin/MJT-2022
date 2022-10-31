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
                      int maxReviewsCount){
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;
        this.reviews = new Review[maxReviewsCount];
    }
    @Override
    public double getRating() {
        if(review_insert_index == 0){
            return 0d;
        }

        double result = 0d;

        int endIDX = review_insert_index % maxReviewsCount;

        for(int i = 0; i < endIDX; ++i){
            result += reviews[i].rating();
        }

        return result / (review_insert_index + 1);
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
        if(review_insert_index >= maxReviewsCount){
            return reviews;
        }
        Review[] result = new Review[review_insert_index];

        System.arraycopy(reviews, 0, result, 0, review_insert_index);

        return result;
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

        reviews[review_insert_index % maxReviewsCount] = review;
        review_insert_index++;
    }

    public Theme getTheme() {
        return theme;
    }

    public double getPriceToPlay() {
        return priceToPlay;
    }
}
