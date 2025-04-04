package com.backend.mobicomm.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.util.stream.Stream;
public class PaymentIdGenerator implements IdentifierGenerator
{
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String query = "SELECT p.paymentId FROM Payment p";
        Stream<String> ids = session.createQuery(query, String.class).stream();
        Long max = ids.map(o -> o.replace("pa_", ""))
                      .mapToLong(Long::parseLong)
                      .max()
                      .orElse(0L);
        return "pa_" + (max + 1);
    }
}
