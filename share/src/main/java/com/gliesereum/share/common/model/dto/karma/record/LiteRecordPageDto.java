package com.gliesereum.share.common.model.dto.karma.record;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class LiteRecordPageDto {

   private List<LiteRecordDto> records = new ArrayList<>();

   private int count = 0;
}
