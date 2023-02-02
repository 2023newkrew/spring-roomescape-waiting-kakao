package nextstep.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Supplier;

@Component
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;

    public TransactionUtil(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void executeTask(Runnable runnable) {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            runnable.run();
            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }


    public <T> T executeTask(Supplier<T> supplier, boolean isReadOnly) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(isReadOnly);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        try {
            T t = supplier.get();
            transactionManager.commit(transaction);

            return t;
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    public <T> T executeReadOnlyTask(Supplier<T> supplier) {
        return executeTask(supplier, true);
    }

}
