package com.lavector.collector.entity.user;

import java.util.List;

/**
 * Service methods for managing {@link User}
 *
 * @author tao
 */
public interface UserService {

    List<User> findAll();

    User find(long id);

    User findOneActive(long id);

    User findByUsername(String username);

    User findByEmail(String email);


    User updateUserPassword(ResetPasswordToken resetPasswordToken, char[] password);

    User update(User user);

    void delete(long userId);

    Integer countUsernameAndActive(Long userId, String username);

}
