package com.gliesereum.share.common.model.response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class MapResponse extends HashMap<String, Object> {

    private static final String DEFAULT_KEY = "result";

    public MapResponse() {
        super();
    }

    public MapResponse(Map<String, Object> map) {
        super(map);
    }

    public MapResponse(Object value) {
        super();
        put(DEFAULT_KEY, value);
    }

    public MapResponse(String key, Object value) {
        super();
        put(key, value);
    }

    public static MapResponse resultTrue() {
        return new MapResponse("true");
    }

    public Builder builder() {
        return this.new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder put(String key, Object value) {
            MapResponse.this.put(key, value);
            return this;
        }

        public MapResponse build() {
            return MapResponse.this;
        }
    }
}
