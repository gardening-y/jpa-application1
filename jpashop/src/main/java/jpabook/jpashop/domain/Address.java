package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

// setter를 제거하고 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들기
// jpa 특성상 embedable은 기본 생성자 필요 -> public보단 protected으로 안전하게
// 이런 제약 두는 이유는 jpa 구현 라이브러리가 객체 생성 시 리플랙션 같은 기술을 사용할 수 있게 지원해야하기 때문
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
