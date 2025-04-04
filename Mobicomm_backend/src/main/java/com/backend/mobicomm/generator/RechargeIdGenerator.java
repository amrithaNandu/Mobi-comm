package com.backend.mobicomm.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.util.stream.Stream;
public class RechargeIdGenerator implements IdentifierGenerator
{
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String query = "SELECT r.rechargeId FROM Recharge r";
        Stream<String> ids = session.createQuery(query, String.class).stream();
        Long max = ids.map(o -> o.replace("re_", ""))
                      .mapToLong(Long::parseLong)
                      .max()
                      .orElse(0L);
        return "re_" + (max + 1);
    }
}
