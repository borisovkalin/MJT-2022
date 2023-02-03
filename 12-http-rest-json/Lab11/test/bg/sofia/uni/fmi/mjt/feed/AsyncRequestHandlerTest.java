package bg.sofia.uni.fmi.mjt.feed;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsyncRequestHandlerTest {

    @Mock
    HttpClient client;
    @Mock
    CompletableFuture<HttpResponse<String>> future;

    @Mock
    HttpResponse<String> response;

    private static HttpRequest request;
    private static URI requestURI;
    @BeforeAll
    static void init() {
        requestURI = URI.create("https://newsapi.org/v2/top-headlines?pageSize=50&page=1&category=&country=&q=Trump+" +
                "&apiKey=");
        request = HttpRequest.newBuilder(requestURI).build();
    }

    @Test
    void testOkResponse() throws ExecutionException, InterruptedException {
        int status = 200;
        when(client.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(future);
        when(future.get()).thenReturn(response);
        when(response.statusCode()).thenReturn(status);

        int result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get().statusCode();
        assertEquals(status, result, "Expected status code 200");
    }

    @Test
    void testOkResponseWithDefaultHttpRequest() throws ExecutionException, InterruptedException {
        int status = 200;
        when(client.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(future);
        when(future.get()).thenReturn(response);
        when(response.statusCode()).thenReturn(status);

        int result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get().statusCode();
        assertEquals(status, result, "Expected status code 200");
    }
}
