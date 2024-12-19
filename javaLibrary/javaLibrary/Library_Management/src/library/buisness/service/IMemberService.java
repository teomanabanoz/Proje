package library.buisness.service;

import library.domain.Member;
import library.domain.enums.MemberType;
import java.util.Set;

public interface IMemberService {
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(int id);
    Member getMemberById(int id);
    Set<Member> getAllMembers();
    Member findMemberByEmail(String email);
    Member findMemberByPhone(String phone);
    Set<Member> findMembersByType(MemberType memberType);
    Set<Member> findMembersByName(String memberName);
    Set<Member> findMembersByGender(String gender);
}