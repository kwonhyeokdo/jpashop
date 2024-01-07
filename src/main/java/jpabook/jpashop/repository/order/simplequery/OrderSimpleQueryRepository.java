package jpabook.jpashop.repository.order.simplequery;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
            """
                SELECT new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)
                  FROM Order o
                  JOIN o.member m
                  JOIN o.delivery d
            """, 
            OrderSimpleQueryDto.class
        ).getResultList();
    }
}
