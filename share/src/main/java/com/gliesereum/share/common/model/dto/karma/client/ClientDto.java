package com.gliesereum.share.common.model.dto.karma.client;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class ClientDto {

    private String id;

    private List<String> corporationIds;

    private List<String> businessIds;

    private String firstName;

    private String lastName;

    private String middleName;

    private String phone;

    private String email;

    private String avatarUrl;
}
