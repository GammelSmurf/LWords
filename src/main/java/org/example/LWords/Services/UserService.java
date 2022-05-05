package org.example.LWords.Services;

import org.example.LWords.Entities.User;

public interface UserService {
    Iterable<User> getUsers();

    void deleteUser(Long id);

    void updateUser(User user);
}
