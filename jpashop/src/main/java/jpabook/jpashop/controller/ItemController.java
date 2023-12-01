package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

//    상품 목록
    @GetMapping(value = "/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

//    상품 수정 폼
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 예제 단순화 위해 캐스팅

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

//    상품 수정
//    @PostMapping(value = "/items/{itemId}/edit")
//    public String updateItem(@ModelAttribute("form") BookForm form) {
////        JPA가 관리하지 않는 준영속 엔티티 -> 변경 감지 기능 사용, 병합 사용
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
////        saveItem에서 getId 있으면 merge -> 병합 사용 방법
//        itemService.saveItem(book);
//        return "redirect:/items";
//    }

    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

//        컨트롤러에서 애매하게 앤티티 사용하지 말고 변경될 값 전달해주거나 DTO 따로 생성해서 넘겨주기
//        트랜젝션이 있는 서비스 계층에서 엔티티 조회, 변경할 것
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        
        return "redirect:/items";
    }
}