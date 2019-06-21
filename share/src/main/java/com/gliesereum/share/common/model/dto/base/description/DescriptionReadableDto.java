package com.gliesereum.share.common.model.dto.base.description;

import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class DescriptionReadableDto<T extends BaseDescriptionDto> extends HashMap<LanguageCode, T> {

    public DescriptionReadableDto(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public DescriptionReadableDto(int initialCapacity) {
        super(initialCapacity);
    }

    public DescriptionReadableDto() {
    }

    public DescriptionReadableDto(Map<? extends LanguageCode, ? extends T> m) {
        super(m);
    }
}
