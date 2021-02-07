package SpotifyInteractions;

import com.microsoft.edge.seleniumtools.EdgeDriver;
import com.microsoft.edge.seleniumtools.EdgeOptions;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;

public class SpotifyAccessTokenGenerator {

    private final String scope = "playlist-read-private playlist-modify-private";

    public String generate(String clientId, String clientSecret, String redirectURI, String username, String password) throws ParseException, SpotifyWebApiException, IOException {

        System.out.println("Generating Spotify Access Token...");

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectURI))
                .build();

        //Generate URI that asks for authorisation from Spotify
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri().scope(scope).build();
        URI uri = authorizationCodeUriRequest.execute();

        //Start up a selenium driver to automatically retrieve the authorisation code
        System.setProperty("webdriver.edge.driver", "D:\\Documents\\Projects\\Java\\edgedriver_win32\\msedgedriver.exe");

        //Don't show browser window
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless");

        WebDriver driver = new EdgeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        String code;
        try {
            driver.get(uri.toString());

            //Login
            driver.findElement(By.id("login-username")).sendKeys(username);
            driver.findElement(By.id("login-password")).sendKeys(password);
            driver.findElement(By.id("login-button")).click();

            //Wait until next screen where the confirm authorisation button is located
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("auth-accept")));
            driver.findElement(By.id("auth-accept")).click();

        } catch (TimeoutException ignored) {
            //auth accept button page was skipped and redirect page was loaded straight after login
        } finally {
            //Now the code can be retrieved from the end of the redirect URL
            String codeURL = driver.getCurrentUrl();
            int codeIndex = codeURL.lastIndexOf("code=");
            code = codeIndex < 0 ? "" : codeURL.substring(codeIndex + "code=".length());

            driver.quit();
        }

        if (code.isEmpty()) throw new ParseException("Invalid login credentials");

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        System.out.println("Spotify Access Token Generated");

        return authorizationCodeCredentials.getAccessToken();
    }
}
