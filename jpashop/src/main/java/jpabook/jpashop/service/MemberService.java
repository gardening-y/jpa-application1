package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 읽기에는 기본적으로 readOnly, 쓰기에는 따로 메서드에 @Transactional 달아줄 것
@Transactional(readOnly = true)
@RequiredArgsConstructor
//@AllArgsConstructor
public class MemberService {
//    @Autowired
//    private MemberRepository memberRepository;
    
////    주입하기가 쉬운 장점, 런타임에 실제 바뀔 수 있다는 것이 단점
//    private MemberRepository memberRepository;
//
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    그래서 이것을 보완
    private final MemberRepository memberRepository;

//    이거 생략하고 @RequiredArgsConstructor ,@AllArgsConstructor 사용가능
//    @Autowired // 생성자 하나면 생략 가능
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //    회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

//    이걸 사용해도 멀티쓰레드 못잡을 수 있으니 Member에서 Unique 제약 조건 걸면 좋음
    private void validateDuplicateMember(Member member) {
//    EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

//    회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
