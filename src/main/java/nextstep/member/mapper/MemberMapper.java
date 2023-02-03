package nextstep.member.mapper;

import auth.domain.UserRole;
import nextstep.member.domain.MemberEntity;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    MemberEntity fromRequest(MemberRequest request, UserRole role);

    MemberResponse toResponse(MemberEntity member);
}