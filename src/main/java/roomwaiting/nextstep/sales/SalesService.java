package roomwaiting.nextstep.sales;

import org.springframework.stereotype.Service;


import static roomwaiting.support.Messages.EMPTY_SALES;

@Service
public class SalesService {
    private final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public Long allAmount(){
        return salesDao.findAllAmount().orElseThrow(()->
                new NullPointerException(EMPTY_SALES.getMessage())
        );
    }


    // 매출 리스트 출력

    // ReservationAdmionService -> Sales +/- 등록

}
