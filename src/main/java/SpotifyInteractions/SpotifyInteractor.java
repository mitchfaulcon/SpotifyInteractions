package SpotifyInteractions;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.wrapper.spotify.SpotifyApi;

import java.util.ArrayList;

public abstract class SpotifyInteractor {

    protected SpotifyApi spotifyApi;

    public SpotifyInteractor(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public abstract void run();

    protected JsonArray createJsonArray(ArrayList<String> idList) {
        StringBuilder sb = new StringBuilder("[");

        for (String id: idList) {
            sb.append("{\"uri\":\"").append(id).append("\"},");
        }

        sb.delete(sb.length() - 1, sb.length());

        sb.append("]");

        return JsonParser.parseString(sb.toString()).getAsJsonArray();
    }
}
