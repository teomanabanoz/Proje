package library.repository;

import library.domain.Member;
import library.domain.enums.MemberType;
import library.repository.base.GenericRepository;
import library.storage.DataStorage;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MemberRepository extends GenericRepository<Member, Set<Member>> implements IMemberRepository {

    @Override
    protected EntityType getEntityType() {
        return EntityType.MEMBER;
    }

    @Override
    public void delete(int id) {
        Member member = DataStorage.getMembers().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
        if (member != null) {
            DataStorage.getMembers().remove(member);
        }
    }

    @Override
    public void update(Member entity) {
        DataStorage.getMembers().remove(entity);
        DataStorage.getMembers().add(entity);
    }

    @Override
    public Optional<Set<Member>> getAll() {
        return Optional.of(new HashSet<>(DataStorage.getMembers()));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findByPhone(String phone) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public Set<Member> findByMemberType(MemberType memberType) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getType().equals(memberType))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Member> getById(int id) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getId() == id)
                .findFirst();
    }

    @Override
    public Set<Member> findByMemberName(String memberName) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getName().equals(memberName))
                .collect(Collectors.toSet());
    }


    @Override
    public Set<Member> findByGender(String gender) {
        return DataStorage.getMembers().stream()
                .filter(member -> member.getGender().equals(gender))
                .collect(Collectors.toSet());
    }
}
