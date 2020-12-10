package edu.phystech.terekhov_na.stickers.security;
import edu.phystech.terekhov_na.stickers.dao.UserDao;
import edu.phystech.terekhov_na.stickers.model.User;
import edu.phystech.terekhov_na.stickers.model.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.Objects;

@Component
@AllArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final UserDao userDao;

    @Override
    public Authentication authenticate(final Authentication authentication)  {
        final UsernamePasswordAuthenticationToken upAuth = (UsernamePasswordAuthenticationToken) authentication;

        User user;
        if(!((String)authentication.getPrincipal()).contains(";")) {
            user = userDao.getUserById((String) authentication.getPrincipal())
                    .getOrElseThrow(BadCredentialsException::new);
        }
        else {
            String[] userLoginOrEmail = StringUtils.split((String) authentication.getPrincipal(), ";");
            user = userDao.getUserByLoginOrEmail(userLoginOrEmail[0], userLoginOrEmail[1])
                    .getOrElseThrow(BadCredentialsException::new);
        }

        final String storedPassword = user.getPassword();

        final String password = (String) upAuth.getCredentials();

        if (Objects.equals(password, "") || !Objects.equals(password, storedPassword)) {
            throw new BadCredentialsException("illegal password");
        }

        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                user.getId().toString(), authentication.getCredentials(),
                Collections.emptyList());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return true;
    }
}