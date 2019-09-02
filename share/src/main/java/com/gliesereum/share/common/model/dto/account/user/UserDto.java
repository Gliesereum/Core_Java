package com.gliesereum.share.common.model.dto.account.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.AuditableDefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AuditableDefaultDto {

    @Size(min = 2)
    @NotEmpty
    private String firstName;

    @Size(min = 2)
    @NotEmpty
    private String lastName;

    @Size(min = 3)
    @NotEmpty
    private String middleName;

    @Size(min = 3)
    @NotEmpty
    private String country;

    @Size(min = 3)
    @NotEmpty
    private String city;

    @Size(min = 6)
    @NotEmpty
    private String address;

    private String addAddress;

    private String avatarUrl;

    private String coverUrl;

    private Gender gender;

    private BanStatus banStatus;

    private Boolean kycApproved;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime lastSignIn;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime lastActivity;

    private List<UUID> corporationIds = new ArrayList<>();

    private String phone;
}
