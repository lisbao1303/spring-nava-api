package com.elisbao.spring_nava_api.repositories.log;

import com.elisbao.spring_nava_api.models.log.OperationLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OperationLogRepository extends ElasticsearchRepository<OperationLog, String> {
}
