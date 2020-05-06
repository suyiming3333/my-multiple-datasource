package com.sym.multipledatasources.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MultiTransactionalAspect
 * @Package com.sym.multipledatasources.datasource
 * @Description: 多数据源事务处理切面，减少重复代码编写
 * @date 2020/5/6 13:37
 */
@Aspect
@Component
public class MultiTransactionalAspect {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger logger = LoggerFactory.getLogger(MultiTransactionalAspect.class);

    @Around(value = "@annotation(com.sym.multipledatasources.datasource.MultiTransactional)")
    public Object transactional(ProceedingJoinPoint point) throws Exception {
        String methodName = point.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
        UserTransaction tran = null;
        Object result = null;
        MultiTransactional multiTransactional = null;
        try {
            Method method = point.getTarget().getClass().getMethod(methodName, parameterTypes);

            if (method.isAnnotationPresent(MultiTransactional.class)) {
                multiTransactional = method.getAnnotation(MultiTransactional.class);
                JtaTransactionManager transactionManager = applicationContext.getBean(JtaTransactionManager.class);
                tran = transactionManager.getUserTransaction();
                tran.begin();
                logger.warn(methodName + ", transaction begin");
                result = point.proceed();
                tran.commit();
                logger.warn(methodName + ", transaction commit");
            }

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);

            if (tran != null) {
                Class<? extends Throwable>[] rollbackExcptions = multiTransactional.rollbackFor();
                Class<? extends Throwable>[] noRollbackExcptions = multiTransactional.noRollbackFor();
                boolean rollback = isPresent(e, rollbackExcptions);
                boolean noRollback = isPresent(e, noRollbackExcptions);

                if (rollback || !noRollback) {
                    tran.rollback();
                    logger.warn(methodName + ", transaction rollback");
                } else {
                    tran.commit();
                    logger.warn(methodName + ", transaction commit");
                }
            }
        }

        return result;
    }

    private boolean isPresent(Throwable e, Class<? extends Throwable>[] excptions) {
        return Arrays.stream(excptions)
                .filter(exception -> e.getClass().isAssignableFrom(exception) || e.getClass().equals(exception))
                .findAny()
                .isPresent();
    }
}
