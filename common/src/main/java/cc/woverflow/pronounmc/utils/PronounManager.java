package cc.woverflow.pronounmc.utils;

import cc.woverflow.pronounmc.PronounMC;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PronounManager {
    private final Gson gson = new Gson();
    private final Cache<UUID, Pronouns> pronounsCache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).maximumSize(500).build();
    private final Set<UUID> inProgressFetching = Sets.newConcurrentHashSet();

    public Pronouns getOrFindPronouns(UUID uuid) {
        if (isCurrentlyFetching(uuid))
            return null;

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
        if (isCurrentlyFetching(uuid))
            throw new IllegalStateException("Already fetching pronouns for " + uuid);

        inProgressFetching.add(uuid);
        PronounMC.getLogger().info("Fetching pronouns for " + uuid.toString());
        URI url = URI.create("https://pronoundb.org/api/v1/lookup?platform=minecraft&id=" + uuid);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(url)
                .header("User-Agent", "Mozilla/5.0 (PronounMC)")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = gson.fromJson(response.body(), JsonObject.class);
        inProgressFetching.remove(uuid);
        return Pronouns.fromId(json.get("pronouns").getAsString());
    }

    public void cachePronounsForServer() {
        for (UUID uuid : Objects.requireNonNull(MinecraftClient.getInstance().player).networkHandler.getPlayerUuids()) {
            Multithreading.runAsync(() -> getOrFindPronouns(uuid));
        }
    }

    public boolean isCurrentlyFetching(UUID uuid) {
        return inProgressFetching.contains(uuid);
    }
}
