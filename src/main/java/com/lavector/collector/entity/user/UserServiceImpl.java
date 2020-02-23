package com.lavector.collector.entity.user;

import com.lavector.collector.entity.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author tao
 */
@Service
@Transactional
class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User find(long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public User findOneActive(long id) {
        return userRepository.findOneActive(id);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmailAndActive(email);
        return user;
    }


    @Override
    public User updateUserPassword(ResetPasswordToken passwordToken, char[] password) {
        if (passwordToken == null) {
            throw new IllegalArgumentException("User password token cannot be null");
        }
        User user = passwordToken.getUser();
        user = resetUserPassword(user, password);
        return user;
    }

    private User resetUserPassword(User user, char[] password) {
        try {
            if (password != null && password.length > 0) {
                user.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
            }
            return userRepository.save(user);
        } finally {
            if (password != null) {
                Arrays.fill(password, 'x');
            }
        }
    }


    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(long userId) {
        logger.info("Deleting user: " + userId);
        User theUser = userRepository.findOneActive(userId);
        if (theUser == null) {
            throw new IllegalArgumentException("no active user found by id:" + userId);
        }
        theUser.setStatus(EntityStatus.DELETED);
        userRepository.save(theUser);
    }

    @Override
    public Integer countUsernameAndActive(Long userId, String username) {
        return userRepository.countEmailAndActive(userId, username.trim().toLowerCase());
    }

}
