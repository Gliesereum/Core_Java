package com.gliesereum.share.common.model.dto.language;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class PackageMapDto {

    private List<LitePackageDto> packages = new ArrayList<>();

    private Map<String, Map<String, String>> phrases = new HashMap<>();
}
