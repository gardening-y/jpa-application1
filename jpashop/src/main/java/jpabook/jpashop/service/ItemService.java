package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

//    변경 감지에 의해 준영속 엔티티 처리 : 영속성 컨텍스트에서 엔티티 조회 후 수정
//    트랜잭션 안에서 엔티티 다시 조회, 변경할 값 선택 -> 트랜잭션 커밋 시점 변경 감지 Dirty Checking
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
//        findItem은 영속 상태
        Item findItem = itemRepository.findOne(itemId);
//        setter없이 엔티티 추적 가능한 방법으로 사용
//       findItem.change(name, price, stockQuantity); 이런식으로 사용할 것

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}