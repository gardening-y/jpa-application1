package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 비즈니스 로직이 대부분 엔티티에 존재 -> 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할
// 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴
// 반대는 트랜잭션 스크립트 패턴

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

//    주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
//      id를 가져와 여기서 무언가를 하는 것이 영속성 컨텍스트에 해당?하므로 더 좋음
        
//        엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

//        배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

//        주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

//        주문 생성
        Order order = Order.createOrder(member,delivery, orderItem);

//        주문 저장
        orderRepository.save(order);
//        order만 저장 (orderitem, delivery은 저장 안됨 -> cascade이고, 다른곳에서 참조하는 것이 없어서 가능)

        return order.getId();
    }

//    주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
//        주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
//        주문 취소
        order.cancel();
//        JPA 장점 -> 알아서 수량 쿼리가 날라감
    }

//    검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
