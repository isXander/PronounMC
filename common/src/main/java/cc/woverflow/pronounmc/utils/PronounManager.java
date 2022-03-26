package cc.woverflow.pronounmc.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PronounManager {
    private final Gson gson = new Gson();
    private final Cache<UUID, Pronouns> pronounsCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).maximumSize(500).build();

    public Pronouns getOrFindPronouns(UUID uuid) {
        try {
            return pronounsCache.get(uuid, () -> findPronouns(uuid));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public Pronouns getCachedPronouns(UUID uuid) {
        return pronounsCache.getIfPresent(uuid);
    }

    private Pronouns findPronouns(UUID uuid) throws IOException, InterruptedException {
        URI url = URI.create("https://pronoundb.org/api/v1/lookup?platform=minecraft&id=" + uuid.toString());

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(url)
                .header("User-Agent", "Mozilla/5.0 (PronounMC)")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = gson.fromJson(response.body(), JsonObject.class);
        return Pronouns.fromId(json.get("pronouns").getAsString());
    }
}
