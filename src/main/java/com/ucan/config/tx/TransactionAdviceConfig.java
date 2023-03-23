package com.ucan.config.tx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @Description: 事务配置（啥玩意？怎么感觉比xml配置还麻烦。。。）
 * @author liming.cen
 * @date 2023-03-21 18:45:56
 * 
 */
@Configuration
@Aspect
public class TransactionAdviceConfig {
    // 切入点表达式
    private static final String POINTCUT_EXPRESSION = "execution(* com.ucan.service..*Impl.*(..))";
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
	RuleBasedTransactionAttribute readOnlyAttribute = new RuleBasedTransactionAttribute();
	readOnlyAttribute.setReadOnly(true);
	readOnlyAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

	RuleBasedTransactionAttribute requiredAttribute = new RuleBasedTransactionAttribute();
	requiredAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
	requiredAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

	Map<String, TransactionAttribute> nameMap = new HashMap<>();
	nameMap.put("query*", readOnlyAttribute);
	nameMap.put("get*", readOnlyAttribute);

	nameMap.put("insert*", requiredAttribute);
	nameMap.put("add*", requiredAttribute);
	nameMap.put("insert*", requiredAttribute);
	nameMap.put("save*", requiredAttribute);
	nameMap.put("update*", requiredAttribute);
	nameMap.put("modify*", requiredAttribute);
	nameMap.put("edit*", requiredAttribute);
	nameMap.put("delete*", requiredAttribute);
	nameMap.put("del*", requiredAttribute);
	nameMap.put("remove*", requiredAttribute);
	NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
	source.setNameMap(nameMap);

	TransactionInterceptor transactionInterceptor = new TransactionInterceptor(platformTransactionManager, source);

	return transactionInterceptor;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	pointcut.setExpression(POINTCUT_EXPRESSION);
	return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
