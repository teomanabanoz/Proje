package library.buisness.service;

import library.domain.Member;
import library.domain.enums.MemberType;
import library.repository.MemberRepository;
import java.util.Optional;
import java.util.Set;

public class MemberService implements IMemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addMember(Member member) {
        validateMember(member);
        validateUniqueFields(member);
        memberRepository.add(member);
    }

    @Override
    public void updateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        // Mevcut üyeyi kontrol et
        Member existingMember = getMemberById(member.getId());

        // Email ve telefon güncellenmişse uniquelik kontrolü
        if (!existingMember.getEmail().equals(member.getEmail())) {
            validateEmailUniqueness(member.getEmail());
        }
        if (!existingMember.getPhone().equals(member.getPhone())) {
            validatePhoneUniqueness(member.getPhone());
        }

        memberRepository.update(member);
    }

    @Override
    public void deleteMember(int id) {
        memberRepository.delete(id);
    }

    @Override
    public Member getMemberById(int id) {
        return memberRepository.getById(id).orElseThrow(() -> new RuntimeException("Member not found"));
    }

    @Override
    public Set<Member> getAllMembers() {
        return memberRepository.getAll().orElseThrow(() -> new RuntimeException("No members found"));
    }

    @Override
    public Member findMemberByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
    }

    @Override
    public Member findMemberByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        Optional<Member> member = memberRepository.findByPhone(phone);
        return member.orElseThrow(() -> new RuntimeException("Member not found with phone: " + phone));
    }

    @Override
    public Set<Member> findMembersByType(MemberType memberType) {
        if (memberType == null) {
            throw new IllegalArgumentException("Member type cannot be null");
        }
        return memberRepository.findByMemberType(memberType);
    }

    @Override
    public Set<Member> findMembersByName(String memberName) {
        if (memberName == null || memberName.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be null or empty");
        }
        return memberRepository.findByMemberName(memberName);
    }

    @Override
    public Set<Member> findMembersByGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        return memberRepository.findByGender(gender);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be null or empty");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Member email cannot be null or empty");
        }
        if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Member phone cannot be null or empty");
        }
        if (member.getType() == null) {
            throw new IllegalArgumentException("Member type cannot be null");
        }
    }

    private void validateUniqueFields(Member member) {
        validateEmailUniqueness(member.getEmail());
        validatePhoneUniqueness(member.getPhone());
    }

    private void validateEmailUniqueness(String email) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("Email already exists: " + email);
        }
    }

    private void validatePhoneUniqueness(String phone) {
        Optional<Member> existingMember = memberRepository.findByPhone(phone);
        if (existingMember.isPresent()) {
            throw new IllegalStateException("Phone number already exists: " + phone);
        }
    }
}
