package roomwaiting.nextstep.sales;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/sales")
public class SalesController {
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/gross")
    public ResponseEntity<Long> grossSales() {
        Long results = salesService.allAmount();
        return ResponseEntity.ok().body(results);
    }
    // 매출 리스트 출력
    @GetMapping
    public ResponseEntity<List<Sales>> lookUp(){
        List<Sales> result = salesService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<Sales>> findByMemberId(@PathVariable Long id){
        List<Sales> result = salesService.findByMemberId(id);
        return ResponseEntity.ok().body(result);
    }

}
