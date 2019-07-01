package com.gliesereum.karma.model.entity.record;

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
public class BaseRecordPageEntity {

   private List<BaseRecordEntity> records = new ArrayList<>();

   private int count = 0;
}
