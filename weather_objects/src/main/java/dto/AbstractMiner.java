package dto;

import lombok.Data;

import java.util.HashMap;

@Data
public abstract class AbstractMiner {
    private final String minerName;
    private final String baseUrl;
    private final String apiKey;

    public String buildUrl(HashMap<String, String> params) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        for (String key : params.keySet())
            url.append(key).append("=").append(params.get(key)).append("&");
        return url.substring(0, url.length() - 1);
    }

    public abstract WeatherSample processResponse(String response);
}
