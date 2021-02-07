package SpotifyInteractions;

import com.google.gson.JsonArray;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlaylistShuffler extends SpotifyInteractor {

    String playlistName;

    public PlaylistShuffler(SpotifyApi spotifyApi, String playlistName) {
        super(spotifyApi);
        this.playlistName = playlistName;
    }

    @Override
    public void run() {

        System.out.println("Shuffling " + playlistName);

        try {
            String playlistId = getPlaylistID(playlistName);

            if (playlistId == null) throw new ParseException("Logged in user does not own a playlist with the given name");

            Paging<PlaylistTrack> playlistTrackPaging = spotifyApi.getPlaylistsItems(playlistId).build().execute();
            ArrayList<String> trackIDs = new ArrayList<>();
            for (PlaylistTrack track: playlistTrackPaging.getItems()) {
                trackIDs.add("spotify:track:" + track.getTrack().getId());
            }

            if (trackIDs.size() == 0) {
                throw new ParseException(playlistName + " has no tracks to shuffle");
            }

            JsonArray trackIDsJson = createJsonArray(trackIDs);
            spotifyApi.removeItemsFromPlaylist(playlistId, trackIDsJson).build().execute();

            Collections.shuffle(trackIDs);
            spotifyApi.addItemsToPlaylist(playlistId, trackIDs.toArray(String[]::new)).build().execute();

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Playlist Shuffle Error: " + e.getMessage());
        }

        System.out.println(playlistName + " completed shuffling");
    }

    private String getPlaylistID(String playlistName) throws ParseException, IOException, SpotifyWebApiException {

        Paging<PlaylistSimplified> playlistSimplifiedPaging = spotifyApi.getListOfCurrentUsersPlaylists().build().execute();

        for (PlaylistSimplified playlist : playlistSimplifiedPaging.getItems()) {
            if (playlistName.toLowerCase().equals(playlist.getName().toLowerCase())) {
                return playlist.getId();
            }
        }

        return null;
    }
}
