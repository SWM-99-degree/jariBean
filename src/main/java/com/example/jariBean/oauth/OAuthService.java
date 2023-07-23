package com.example.jariBean.oauth;

import com.example.jariBean.entity.User;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.jariBean.entity.User.UserRole.CUSTOMER;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;

    public String createSocialId(String registration, String id) {
        return registration + "_" + id;
    }

    public User findByUserSocialId(String userSocialId, String userNickname, String userImageUrl) {
        return userRepository.findByUserSocialId(userSocialId).orElse(
                User.builder()
                        .userSocialId(userSocialId)
                        .userImageUrl(userImageUrl)
                        .userNickname(userNickname)
                        .userRole(CUSTOMER)
                        .build()
        );
    }

}
