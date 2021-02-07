package SpotifyInteractions;

import com.wrapper.spotify.SpotifyApi;

public class SpotifyInteractorFactory {

    SpotifyApi spotifyApi;

    public SpotifyInteractorFactory(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public SpotifyInteractor buildInteractor(String argName) {
        return buildInteractor(argName, null);
    }

    public SpotifyInteractor buildInteractor(String argName, String[] params) {

        switch (argName) {
            case "s":
                return new PlaylistShuffler(spotifyApi, params[0]);
            case "u":
            case "p":
            default:
                return null;
        }
    }
}
