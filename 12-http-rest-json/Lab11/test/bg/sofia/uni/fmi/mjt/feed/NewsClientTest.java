package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.error.ErrorResponse;
import bg.sofia.uni.fmi.mjt.feed.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.feed.exception.IllegalParameterInputException;
import bg.sofia.uni.fmi.mjt.feed.util.Parameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class NewsClientTest {

    @Mock
    private NewsClient mockedClient;

    AsyncRequestHandler requestHandler;
    ResponseHandlerTest responseHandler;

    private static Parameters params;

    private static final NewsClient client = new NewsClient();
    @BeforeEach
    void test() {
        params = new Parameters();
        params.setKeys("Donald");
        responseHandler = spy(new ResponseHandlerTest());
        requestHandler = spy(AsyncRequestHandler.getInstance());
    }
    @Test
    void testTooManyRequestResponse() {
        when(mockedClient.retrieveNewsPage())
                .thenThrow(new BadResponseException(
                        ErrorResponse.TOO_MANY_REQUESTS.code, ErrorResponse.TOO_MANY_REQUESTS.message));

        assertThrows(BadResponseException.class,() -> mockedClient.retrieveNewsPage(),
                "An exception was expected when sending more than 100 requests");
    }


    @Test
    void testOkResultWithNoElements() {
        when(mockedClient.retrieveNewsPage()).thenReturn(new ArrayList<>());

        assertTrue(mockedClient.retrieveNewsPage().isEmpty()," Expected an empty list when there are 0 results");
    }

    @Test
    void testRetrieveResponseWithNoParameters() {
        assertThrows(IllegalParameterInputException.class, client::retrieveNewsPage,
                "Expected an exception to be thrown when the parameters are not set");
    }

    @Test
    void testRetrieveResponseWithNOTOKENURI() {

        assertThrows(BadResponseException.class, () -> {
            client.setParams(params);
            client.retrieveNewsPage();
        },"Expected an exception to be thrown when there is no Designated API key");
    }

}
