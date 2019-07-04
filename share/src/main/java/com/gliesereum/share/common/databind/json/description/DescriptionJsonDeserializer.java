package com.gliesereum.share.common.databind.json.description;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class DescriptionJsonDeserializer extends JsonDeserializer<List<? extends BaseDescriptionDto>> implements ContextualDeserializer {

    private JavaType javaType;

    @Override
    public List<? extends BaseDescriptionDto> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        Map<LanguageCode, Object> map = mapper.readValue(p, new TypeReference<Map<LanguageCode, Object>>() {
        });
        List<BaseDescriptionDto> result = null;
        if (MapUtils.isNotEmpty(map)) {
            result = map.entrySet().stream()
                    .filter(i -> ObjectUtils.allNotNull(i.getKey(), i.getValue()))
                    .map(i -> {
                        BaseDescriptionDto description = mapper.convertValue(i.getValue(), javaType);
                        description.setLanguageCode(i.getKey());
                        return description;
                    }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        JavaType wrapperType = property.getType();
        JavaType valueType = wrapperType.containedType(0);
        DescriptionJsonDeserializer descriptionJsonDeserializer = new DescriptionJsonDeserializer();
        descriptionJsonDeserializer.javaType = valueType;
        return descriptionJsonDeserializer;
    }
}
