package com.gliesereum.karma.facade.worker.impl;

import com.gliesereum.karma.facade.worker.WorkerFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.share.common.exchange.service.mail.MailExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Service
@Slf4j
public class WorkerFacadeImpl implements WorkerFacade {

    private final String workerInfoMessage = "Vash nomer telefonu buv vikoristaniy dlya dodavannya spivrobitnika v kompaniyu: ";

    @Autowired
    private BaseBusinessService businessService;

    @Autowired
    private MailExchangeService mailExchangeService;

    @Override
    @Async
    public void sendMessageToWorkerAfterCreate(WorkerDto worker) {
        if (worker != null && worker.getUser() != null) {
            StringBuilder message = new StringBuilder(workerInfoMessage);
            LiteBusinessDto business = businessService.getLiteById(worker.getBusinessId());
            if (business != null) {
                message.append(business.getName()).append(" tell: +").append(business.getPhone());
            }
            mailExchangeService.sendMessagePhone(worker.getUser().getPhone(), message.toString());
        }
    }
}
