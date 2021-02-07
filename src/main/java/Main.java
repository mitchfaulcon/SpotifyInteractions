import CLI.CommandLineReader;
import SpotifyInteractions.SpotifyAccessTokenGenerator;
import SpotifyInteractions.SpotifyInteractorFactory;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import org.apache.commons.cli.*;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.Arrays;


public class Main {

    private static final String _clientID = "7ca4e40a0bce43848e8244d78e51aae5";
    private static final String _clientSecret = "fb3f7df3d7c5430e9d1e784f9f32410a";
    private static final String _redirectURI = "https://www.example.com";

    public static void main(String[] args) {

        CommandLineReader cmdReader = new CommandLineReader();
        CommandLine cmd = null;
        try {
            cmd = new CommandLineReader().parseArgs(args);
        } catch (org.apache.commons.cli.ParseException e) {
            //System.out.println("Error: " + e.getMessage());
        }

        if (cmd == null || cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar SpotifyInteractions-1.0-jar-with-dependencies.jar", cmdReader.buildOptions());

            return;
        }

        String username = cmd.getOptionValue("u");
        String password = cmd.getOptionValue("p");

        String accessToken = null;
        try {
            accessToken = new SpotifyAccessTokenGenerator().generate(_clientID, _clientSecret, _redirectURI, username, password);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();


        SpotifyInteractorFactory interactorFactory = new SpotifyInteractorFactory(spotifyApi);
        for (Option option: cmd.getOptions()) {
            try {
                interactorFactory.buildInteractor(option.getOpt(), option.getValues()).run();
            } catch (NullPointerException ignored) {
                //Option doesn't have a run implementation (i.e. -help, -username, -password)
            }
        }
    }
}
