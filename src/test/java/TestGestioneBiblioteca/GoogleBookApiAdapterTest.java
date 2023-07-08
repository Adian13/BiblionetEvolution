package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.GoogleBookApiAdapterImpl;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GoogleBookApiAdapterTest {

    @Test
    public void testGetLibroDaBookApi() throws Exception {
        // Mock HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(getMockResponse().getBytes()));

        // Mock URL
        URL url = Mockito.mock(URL.class);
        when(url.openConnection()).thenReturn(connection);

        GoogleBookApiAdapterImpl adapter = new GoogleBookApiAdapterImpl();
        adapter.googleApiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        LibroBiblioteca libro = new LibroBiblioteca();
        libro.setIsbn("9780140328721");

        LibroBiblioteca result = adapter.getLibroDaBookApi("9780140328721", libro);

        assertNotNull(result);
        assertEquals("Charlotte's Web", result.getTitolo());
        assertEquals("Questo libro parla di Animals, Fantasy, Friendship, Humor", result.getDescrizione());
        assertEquals("Harper & Row", result.getCasaEditrice());
        assertEquals("E. B. White", result.getAutore());
        assertEquals(LocalDateTime.of(1974, 1, 1, 0, 0), result.getAnnoDiPubblicazione());
        assertEquals("9780140328721", result.getIsbn());
        assertNotNull(result.getImmagineLibro());
    }

    private String getMockResponse() {
        JSONObject jsonData = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject bookInfo = new JSONObject();
        JSONObject volumeInfo = new JSONObject();
        JSONArray categories = new JSONArray();
        categories.add("Animals");
        categories.add("Fantasy");
        categories.add("Friendship");
        categories.add("Humor");
        JSONObject images = new JSONObject();
        images.put("smallThumbnail", "https://example.com/image.jpg");

        volumeInfo.put("title", "Charlotte's Web");
        volumeInfo.put("publisher", "Harper & Row");
        volumeInfo.put("publishedDate", "1974");
        volumeInfo.put("categories", categories);
        volumeInfo.put("authors", new JSONArray().add("E. B. White"));
        volumeInfo.put("imageLinks", images);

        bookInfo.put("volumeInfo", volumeInfo);
        items.add(bookInfo);
        jsonData.put("items", items);

        return jsonData.toJSONString();
    }
}