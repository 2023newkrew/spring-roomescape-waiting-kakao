package nextstep.member.mapper;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberEntity;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    Member fromRequest(MemberRequest request);

    MemberResponse toResponse(MemberEntity member);
}