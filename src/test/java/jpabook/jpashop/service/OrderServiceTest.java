package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.excption.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        // given
        final String memberName = "회원1";
        final Address memberAddress = new Address("서울", "강가", "123-123");
        Member member = createMember(memberName, memberAddress);

        final String bookName = "시골 JPA";
        final int bookPrice = 10000;
        final int bookStockQuantity = 10;
        Book book = createBook(bookName, bookPrice, bookStockQuantity);

        final int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", bookPrice * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고 수가 줄어야 한다.", bookStockQuantity - orderCount, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception{
        // given
        final String memberName = "회원1";
        final Address memberAddress = new Address("서울", "강가", "123-123");
        Member member = createMember(memberName, memberAddress);

        final String bookName = "시골 JPA";
        final int bookPrice = 10000;
        final int bookStockQuantity = 10;
        Book book = createBook(bookName, bookPrice, bookStockQuantity);

        final int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", bookStockQuantity, book.getStockQuantity());
    }
    
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        // given
        final String memberName = "회원1";
        final Address memberAddress = new Address("서울", "강가", "123-123");
        Member member = createMember(memberName, memberAddress);

        final String bookName = "시골 JPA";
        final int bookPrice = 10000;
        final int bookStockQuantity = 10;
        Book book = createBook(bookName, bookPrice, bookStockQuantity);

        final int orderCount = 11;

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    private Member createMember(String name, Address address){
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);

        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }
}
