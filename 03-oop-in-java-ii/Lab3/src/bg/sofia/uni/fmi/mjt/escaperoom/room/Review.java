package bg.sofia.uni.fmi.mjt.escaperoom.room;

public record Review(int rating, String reviewText) {

    public Review{
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("The rating doesn't correspond with the restrictions [0,10]");
        }

        if(reviewText == null || reviewText.length() > 200){
            throw new IllegalArgumentException("The reviewText is either null or longer than 200 symbols");
        }

    }
}
