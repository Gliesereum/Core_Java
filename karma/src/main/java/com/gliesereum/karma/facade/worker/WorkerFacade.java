package com.gliesereum.karma.facade.worker;

import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WorkerFacade {

    void sendMessageToWorkerAfterCreate(WorkerDto worker);
}
