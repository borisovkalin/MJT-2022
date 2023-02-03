package bg.sofia.uni.fmi.mjt.feed.response;

import java.util.List;

public record NewsResponse(String status, int totalResults, List<Article> articles) {
}
