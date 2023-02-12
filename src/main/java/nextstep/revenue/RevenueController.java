package nextstep.revenue;

import auth.annotation.LoginMember;
import auth.domain.UserDetails;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/admin/revenues")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<List<Revenue>> findAll(@LoginMember UserDetails member) {
        List<Revenue> revenues = revenueService.findAll(new Member(member));
        return ResponseEntity.ok(revenues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenue> findById(@LoginMember UserDetails member,
                                            @PathVariable("id") @NotNull @Min(1L) Long id) {
        Revenue revenue = revenueService.findById(new Member(member), id);
        return ResponseEntity.ok(revenue);
    }
}
