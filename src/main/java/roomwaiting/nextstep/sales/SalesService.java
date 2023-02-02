package roomwaiting.nextstep.sales;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SalesService {
    private final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public Long allAmount(){
        return salesDao.findAllAmount().orElse(0L);
    }

    public List<Sales> findAll() {
        return salesDao.findAllSales();
    }

    // 매출 리스트 출력

}
