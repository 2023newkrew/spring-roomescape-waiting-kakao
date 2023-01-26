package nextstep.member.mapper;

import auth.domain.UserRole;
import nextstep.member.domain.Member;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    Member fromRequest(MemberRequest request, UserRole role);

    MemberResponse toResponse(Member reservation);
}