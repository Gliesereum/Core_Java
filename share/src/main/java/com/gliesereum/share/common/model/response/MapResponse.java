package com.gliesereum.share.common.model.response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 30/11/2018
 */
public class MapResponse extends HashMap<String, String> {

    private static final String DEFAULT_KEY = "result";

    public MapResponse() {
        super();
    }

    public MapResponse(Map<String, String> map) {
        super(map);
    }

    public MapResponse(String value) {
        super();
        put(DEFAULT_KEY, value);
    }

    public MapResponse(String key, String value) {
        super();
        put(key, value);
    }

    public Builder builder() {
        return this.new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder put(String key, String value) {
            MapResponse.this.put(key, value);
            return this;
        }

        public MapResponse build() {
            return MapResponse.this;
        }
    }
}
