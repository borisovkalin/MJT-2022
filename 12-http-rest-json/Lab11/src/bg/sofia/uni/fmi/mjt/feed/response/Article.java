package bg.sofia.uni.fmi.mjt.feed.response;

import java.util.Map;

public record Article(Map<String, String> source, String author, String title, String description,
                      String url, String urlToImage, String publishedAt, String content) {
}
