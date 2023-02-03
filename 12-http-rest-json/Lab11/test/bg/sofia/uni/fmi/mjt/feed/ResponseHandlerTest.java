package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.feed.response.NewsResponse;
import bg.sofia.uni.fmi.mjt.feed.response.Page;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResponseHandlerTest {
    @Mock
    CompletableFuture<HttpResponse<String>> future;

    @Mock
    HttpResponse<String> httpResponse;
    @Mock
    ResponseHandler mockedResponseHandler;
    private final ResponseHandler responseHandler = new ResponseHandler();

    private static final String I_DONT_HAVE_TIME_FOR_THESE_TESTS = "{\"status\":\"ok\",\"totalResults\":34,\"articles\":[{\"source\":{\"id\":null,\"name\":\"CNET\"},\"author\":\"Amanda Kooser\",\"title\":\"Sweeping New Milky Way Portrait Captures More Than 3 Billion Stars - CNET\",\"description\":\"Behold a \\\"gargantuan astronomical data tapestry\\\" of our glorious galaxy.\",\"url\":\"https://www.cnet.com/science/space/sweeping-new-milky-way-portrait-captures-more-than-3-billion-stars/\",\"urlToImage\":\"https://www.cnet.com/a/img/resize/80a9a5e41cb05ffa1aec0686c4960ce26433d4a6/hub/2023/01/19/4219393c-cbb2-4e32-afd3-403a8e145580/noirlab2301a.jpg?auto=webp&fit=crop&height=630&width=1200\",\"publishedAt\":\"2023-01-22T18:14:00Z\",\"content\":\"How many stars can you count when you look up into the clear night sky? Not nearly as many as the Dark Energy Camera in Chile. Scientists released a survey of a portion of our home Milky Way galaxy t… [+2444 chars]\"}]}";

    private final Gson gson = new Gson();

    @Test
    void testHandleNonNullRunner() {
        NewsResponse news = gson.fromJson(I_DONT_HAVE_TIME_FOR_THESE_TESTS, NewsResponse.class);
        when(mockedResponseHandler.handle(future)).thenReturn(new Page(news.articles()));

        assertNotNull(mockedResponseHandler.handle(future),"Expected non null article");
    }

    @Test
    void testHandleCorrectArticle() throws ExecutionException, InterruptedException {
        when(future.get()).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(I_DONT_HAVE_TIME_FOR_THESE_TESTS);

        assertEquals(1,responseHandler.handle(future).articles().size(),"Expected one article");
    }

    @Test
    void testExtractResponseInterrupted() throws ExecutionException, InterruptedException {
        when(future.get()).thenThrow(new InterruptedException("Interrupted"));
        assertThrows(RuntimeException.class, ()-> responseHandler.handle(future),"Expected one article");
    }

    @Test
    void testHandleWithServerError() throws ExecutionException, InterruptedException {
        when(future.get()).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(BadResponseException.class,() -> responseHandler.handle(future), "Expected correct error code");
    }

}
