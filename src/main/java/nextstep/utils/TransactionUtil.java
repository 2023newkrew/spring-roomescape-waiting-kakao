package nextstep.utils;

import nextstep.error.ApplicationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Supplier;

import static nextstep.error.ErrorType.INTERNAL_SERVER_ERROR;

@Component
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;

    public TransactionUtil(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public <T> T executeReadOnlyTask(Supplier<T> supplier) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(true);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        try {
            T t = supplier.get();
            transactionManager.commit(transaction);

            return t;
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new ApplicationException(INTERNAL_SERVER_ERROR);
        }
    }

}
