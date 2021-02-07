import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestArgs {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public String[] generateArgs(String commandlineInput) {
        return commandlineInput.split(" ");
    }

    @Test
    public void TestNoArgs() {
        Main.main(generateArgs(""));
        
        assertEquals("usage: java -jar SpotifyInteractions-1.0-jar-with-dependencies.jar\r\n" +
                " -help                          display command help\r\n" +
                " -p,--password <password>       Spotify password (required)\r\n" +
                " -s,--shuffle <Playlist Name>   Shuffle input playlist (User credentials\r\n" +
                "                                must have edit permission)\r\n" +
                " -u,--username <username>       Spotify username (required)\r\n", outContent.toString());
    }

    @Test
    public void TestHelp() {
        Main.main(generateArgs("-help"));

        assertEquals("usage: java -jar SpotifyInteractions-1.0-jar-with-dependencies.jar\r\n" +
                " -help                          display command help\r\n" +
                " -p,--password <password>       Spotify password (required)\r\n" +
                " -s,--shuffle <Playlist Name>   Shuffle input playlist (User credentials\r\n" +
                "                                must have edit permission)\r\n" +
                " -u,--username <username>       Spotify username (required)\r\n", outContent.toString());
    }

    @Test
    public void TestUsernameOnly() {
        Main.main(generateArgs("-u Username"));

        assertEquals("usage: java -jar SpotifyInteractions-1.0-jar-with-dependencies.jar\r\n" +
                " -help                          display command help\r\n" +
                " -p,--password <password>       Spotify password (required)\r\n" +
                " -s,--shuffle <Playlist Name>   Shuffle input playlist (User credentials\r\n" +
                "                                must have edit permission)\r\n" +
                " -u,--username <username>       Spotify username (required)\r\n", outContent.toString());
    }

    @Test
    public void TestPasswordOnly() {
        Main.main(generateArgs("-p password"));

        assertEquals("usage: java -jar SpotifyInteractions-1.0-jar-with-dependencies.jar\r\n" +
                " -help                          display command help\r\n" +
                " -p,--password <password>       Spotify password (required)\r\n" +
                " -s,--shuffle <Playlist Name>   Shuffle input playlist (User credentials\r\n" +
                "                                must have edit permission)\r\n" +
                " -u,--username <username>       Spotify username (required)\r\n", outContent.toString());
    }

    @Test
    public void TestUsernameAndPassword() {
        Main.main(generateArgs("-u Username -p password"));
        assertEquals("Generating Spotify Access Token...\r\n" +
                "Error: Invalid login credentials\r\n", outContent.toString());
    }
}
