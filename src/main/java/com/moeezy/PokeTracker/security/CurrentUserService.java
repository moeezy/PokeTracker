package com.moeezy.PokeTracker.security;

import com.moeezy.PokeTracker.data.entity.User;
import com.moeezy.PokeTracker.data.repository.UserRepository;
import com.moeezy.PokeTracker.web.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// The JWT only carries a username (see JwtTokenProvider) - there is no userId claim and no
// built-in link between the authenticated principal and a {userId} path variable. Without this
// check, any authenticated user could read/write any other user's data by changing the userId
// in the URL. Every controller endpoint that takes a {userId} path variable must call
// verifyIsSelf before touching that user's data.
@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public void verifyIsSelf(long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user == null || user.getUserId() != userId) {
            throw new ForbiddenException("Not authorized to access this user's data");
        }
    }
}
