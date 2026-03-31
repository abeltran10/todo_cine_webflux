package com.todocine_webflux.dao.impl;

import com.todocine_webflux.dao.GanadorRepo;
import com.todocine_webflux.entities.Premio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GanadorRepoImpl implements GanadorRepo {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Premio> findByPremioIdAndAnyo(String premioId, Integer anyo, int offset, int limit) {
        Query query = new Query()
                .addCriteria(Criteria.where("premioId").is(premioId).and("anyo").is(anyo))
                .skip(offset)
                .limit(limit);

        return mongoTemplate.find(query, Premio.class);
    }

    @Override
    public Mono<Long> countByPremioIdAndAnyo(String premioId, Integer anyo) {
        Query query = new Query()
                .addCriteria(Criteria.where("premioId").is(premioId).and("anyo").is(anyo));

        return mongoTemplate.count(query, Premio.class);
    }
}
