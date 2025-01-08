package com.elisbao.spring_nava_api.aspects;

import com.elisbao.spring_nava_api.annotations.LogOperation;
import com.elisbao.spring_nava_api.models.log.OperationLog;
import com.elisbao.spring_nava_api.repositories.log.OperationLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
public class LoggingAspect {

    private final OperationLogRepository operationLogRepository;

    public LoggingAspect(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @Around("@annotation(logOperation)")
    public Object logOperation(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {

        OperationLog log = new OperationLog();
        log.setOperationName(joinPoint.getSignature().getName()); // Nome da operação
        log.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()); // Metodo completo
        log.setTimestamp(LocalDateTime.now()); // Timestamp inicial

        // Log de sucesso inicialmente como falso
        log.setSuccess(false);


        Object result = null;
        try {

            result = joinPoint.proceed();
            log.setSuccess(true);
            log.setResponseData(result);

        } catch (Exception e) {
            log.setSuccess(false);
            log.setErrorDetails(e.getMessage());
            throw e;
        } finally {

            // Salva o log no Elasticsearch
            operationLogRepository.save(log);
        }

        return result;
    }

}
