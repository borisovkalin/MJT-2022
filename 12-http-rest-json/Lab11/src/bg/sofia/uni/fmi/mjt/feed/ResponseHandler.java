package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.feed.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.feed.response.NewsResponse;
import bg.sofia.uni.fmi.mjt.feed.response.Page;
import com.google.gson.Gson;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ResponseHandler {
    private final Gson converter;

    public ResponseHandler() {
        converter = new Gson();
    }

    public Page handle(CompletableFuture<HttpResponse<String>> response) {
        HttpResponse<String> newsResponse = extractResponse(response);
        errorCheck(newsResponse);

        NewsResponse news = converter.fromJson(newsResponse.body(), NewsResponse.class);

        return new Page(news.articles());
    }

    private HttpResponse<String> extractResponse(CompletableFuture<HttpResponse<String>> response) {
        try {
            return response.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Internal Proxy error 500 Something happened " +
                    "while trying to get response from API", e);
        }
    }

    public static void errorCheck(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if (statusCode == ErrorResponse.OK.code) {
            return;
        }

        for (ErrorResponse err: ErrorResponse.values()) {
            if (err.code == statusCode) {
                throw new BadResponseException(statusCode, err.message);
            }
        }

    }
}
