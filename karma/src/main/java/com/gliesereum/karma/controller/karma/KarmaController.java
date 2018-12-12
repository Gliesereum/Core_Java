package com.gliesereum.karma.controller.karma;

import com.gliesereum.karma.service.karma.KarmaService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.karma.KarmaDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-10
 */

@RestController
@RequestMapping("/karma")
public class KarmaController {

    @Autowired
    private KarmaService karmaService;

    @GetMapping("/me")
    public KarmaDto userKarma() {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return karmaService.getByUserId(userId);
    }
}
