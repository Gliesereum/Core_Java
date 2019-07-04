package com.gliesereum.share.common.databind.json.description;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class DescriptionJsonSerializer extends JsonSerializer<List<? extends BaseDescriptionDto>> {

    @Override
    public void serialize(List<? extends BaseDescriptionDto> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (CollectionUtils.isNotEmpty(value)) {
            Map<LanguageCode, ? extends BaseDescriptionDto> map = value.stream().collect(Collectors.toMap(BaseDescriptionDto::getLanguageCode, i -> i));
            gen.writeObject(map);
        } else {
            gen.writeNull();
        }
    }
}
