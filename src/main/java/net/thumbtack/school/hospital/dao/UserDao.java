package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.model.User;

public interface UserDao {
    String logInUser(String login, String password) throws ServerException;
    void logOutUser(User user);
    User getByToken(String token) throws ServerException;
    void updatePasswordUser(User user, String password) throws ServerException;
}
