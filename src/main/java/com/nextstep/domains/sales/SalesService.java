package com.nextstep.domains.sales;


import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.sales.enums.SalesStatus;
import com.nextstep.interfaces.sales.dtos.SalesMapper;
import com.nextstep.interfaces.sales.dtos.SalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SalesService {

    private final SalesRepository repository;

    private final SalesMapper mapper;

    @Transactional
    public SalesResponse approveByReservationId(Reservation reservation) {
        Sales sales = new Sales(null, reservation, reservation.getSchedule().getTheme().getPrice(), SalesStatus.APPROVE);
        return mapper.toResponse(repository.insert(sales));
    }

    public boolean cancelByReservationId(Long reservationId) {
        Sales sales = repository.getByReservationId(reservationId);
        sales.cancel();
        return repository.updateById(sales);
    }

    public List<SalesResponse> getAll(){
        return repository.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}
