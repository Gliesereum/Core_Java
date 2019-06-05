package com.gliesereum.karma.service.business;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface DataLoadingService {

    void createBusiness(String rightTop, String leftBottom);

    void createRecords();

    void createRecordsByBusinessId(UUID businessId);
}
