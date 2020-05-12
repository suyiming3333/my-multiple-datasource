package com.sym.multipledatasources.datasource;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MultiTransactional
 * @Package com.sym.multipledatasources.datasource
 * @Description: 用于跨库事务，仅仅可用于方法上，表明当前方法所有操作属于同一个事务
 * @date 2020/5/6 13:36
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MultiTransactional {

    @AliasFor("transactionManager")
    String value() default "";

    @AliasFor("value")
    String transactionManager() default "";

    Class<? extends Throwable>[] rollbackFor() default {};

    Class<? extends Throwable>[] noRollbackFor() default {};
}
