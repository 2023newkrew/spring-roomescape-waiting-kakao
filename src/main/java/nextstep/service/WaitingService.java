package nextstep.service;

import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.response.WaitingResponse;
import nextstep.domain.persist.Waiting;
import nextstep.repository.WaitingDao;
import nextstep.support.exception.api.waiting.NotWaitingOwnerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.support.converter.UserDetailToMemberConverter.convertUserDetailToMember;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingDao waitingDao;

    @Transactional(readOnly = true)
    public List<WaitingResponse> findAll(Long id) {
        return waitingDao.findAll(id).stream()
                .map(waiting -> (new WaitingResponse(waiting, getPriority(waiting))))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeWaiting(UserDetails userDetails, Long id) {
        Waiting waiting = waitingDao.findById(id);

        if (isNotWaitingOwner(userDetails, waiting)) {
            throw new NotWaitingOwnerException();
        }

        waitingDao.deleteById(id);
    }

    private boolean isNotWaitingOwner(UserDetails userDetails, Waiting waiting) {
        return !waiting.sameMember(convertUserDetailToMember(userDetails));
    }

    private Long getPriority(Waiting waiting) {
        return waitingDao.getPriority(waiting.getSchedule().getId(), waiting.getId());
    }
}
