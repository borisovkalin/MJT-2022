package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.response.Page;
import bg.sofia.uni.fmi.mjt.feed.util.Parameters;
import bg.sofia.uni.fmi.mjt.feed.util.URICreator;
import bg.sofia.uni.fmi.mjt.feed.validator.ParameterValidator;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class NewsClient {

    private static final int TIMEOUT_DURATION = 20;
    private static final int MAX_ARTICLE_CAPACITY = 50;
    private final AsyncRequestHandler asyncRequestHandler;

    private final ResponseHandler responseHandler;

    private final HttpClient client;

    private final URICreator creator;

    private Parameters params;


    public NewsClient() {
        this.creator = new URICreator();
        this.client = createClient();
        this.responseHandler = new ResponseHandler();
        this.asyncRequestHandler = AsyncRequestHandler.getInstance();
        this.params = new Parameters();
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public List<Page> retrieveNewsPage() {
        params.incrementPage();
        List<Page> result = getNews();
        params.clearParameters();
        return result;
    }

    private List<Page> getNews() {
        ParameterValidator.checkRequestParams(params.getKeys().toString());

        URI currentUri = creator.create(params.getCategory(), params.getCountry(),
                params.getKeys().toString(), params.getPage());

        List<Page> result = new ArrayList<>();
        Page first = retrieveResponse(currentUri);
        result.add(first);

        if (first.articles().size() == MAX_ARTICLE_CAPACITY) {
            result.add(getNextResponse());
        }

        return result;
    }

    private Page getNextResponse() {
        ParameterValidator.checkRequestParams(params.getKeys().toString());

        URI currentUri = creator.nextPage();

        return retrieveResponse(currentUri);
    }


    private Page retrieveResponse(URI currentUri) {
        var future = asyncRequestHandler.sendRequest(currentUri, client);
        return responseHandler.handle(future);
    }

    private HttpClient createClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(TIMEOUT_DURATION))
                .build();
    }
}
