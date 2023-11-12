package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 이게 있으면 기본 롤백을 해서 @Rollback(false) 따로 안하면 insert문 안나감
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("garden");

        // when
        Long saveId = memberService.join(member);

//        db트렌젝션이 커밋을 하는 순간 플러쉬가 되면서 jpa 영속성 컨텍스트에 있는 member 객체가 만들어지면서 insert문이 나감?
        // then
        em.flush(); //db에 쿼리가 가는 것
//        영속성 컨텍스트에 member객체 들어감 -> 쿼리로 db에 반영 됨
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("gardeny");

        Member member2 = new Member();
        member2.setName("gardeny");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 함
//        try {
//            memberService.join(member2); // 예외가 발생해야 함
//        } catch (IllegalStateException e) {
//            return;
//        }

        // then
        fail("예외가 발생해야 한다");

    }

}