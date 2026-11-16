package com.bootcamp;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/** Socios en memoria. En los tests del controlador se sustituye por un mock. */
@Service
public class MemberService {

    private final Map<String, Member> byEmail = new ConcurrentHashMap<>();

    public Member register(MemberRequest request) {
        if (byEmail.containsKey(request.email())) {
            throw new DuplicateMemberException(request.email());
        }
        Member member = new Member(UUID.randomUUID().toString(), request.name(), request.email(), request.birthYear());
        byEmail.put(member.email(), member);
        return member;
    }
}
