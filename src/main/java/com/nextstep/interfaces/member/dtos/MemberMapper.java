package com.nextstep.interfaces.member.dtos;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.nextstep.domains.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    Member fromRequest(MemberRequest request, RoleType role);

    MemberResponse toResponse(Member reservation);
}