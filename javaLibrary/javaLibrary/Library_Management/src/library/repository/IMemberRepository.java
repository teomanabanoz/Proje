package library.repository;

import library.domain.Member;
import library.domain.enums.MemberType;
import library.repository.base.IGenericRepository;
import java.util.Optional;
import java.util.Set;

public interface IMemberRepository extends IGenericRepository<Member, Set<Member>> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone);

    Set<Member> findByMemberType(MemberType memberType);
    Set<Member> findByMemberName(String memberName);
    Set<Member> findByGender(String gender);
}
