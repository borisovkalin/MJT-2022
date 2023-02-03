package bg.sofia.uni.fmi.mjt.feed;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AsyncRequestHandler {

    private static final String RESPONSE_TYPE = "application/json";

    private static final String CONTENT_TYPE = "Content-Type";

    private static AsyncRequestHandler client;

    private AsyncRequestHandler() { }

    public static AsyncRequestHandler getInstance() {
        if (client == null) {
            client = new AsyncRequestHandler();
        }
        return client;
    }

    public CompletableFuture<HttpResponse<String>> sendRequest(URI requestURI, HttpClient client) {

        HttpRequest request = createRequest(requestURI);

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder().uri(uri)
                .header(CONTENT_TYPE, RESPONSE_TYPE)
                .build();
    }
}
