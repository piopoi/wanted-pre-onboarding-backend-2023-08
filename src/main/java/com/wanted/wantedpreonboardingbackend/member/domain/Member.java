package com.wanted.wantedpreonboardingbackend.member.domain;

import static com.wanted.wantedpreonboardingbackend.member.constants.MemberConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@ToString
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String role = Role.USER.getValue();

    public Member() {
    }

    @Builder
    public Member(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    private void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException(MEMBER_EMAIL_INVALID);
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException(MEMBER_PASSWORD_LENGTH_MIN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Member member = (Member) o;

        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
