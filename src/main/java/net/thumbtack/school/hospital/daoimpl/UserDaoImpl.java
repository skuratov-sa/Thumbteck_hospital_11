package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.db.DataBase;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.model.User;

public class UserDaoImpl implements UserDao {
    @Override
    public String logInUser(String login, String password) throws ServerException {
        return DataBase.getInstance().logInUser(login,password);
    }

    @Override
    public void logOutUser(User user) {
        DataBase.getInstance().logOutUser(user);
    }

    @Override
    public User getByToken(String token) throws ServerException {
        return DataBase.getInstance().getByTokenUser(token);
    }

    @Override
    public void updatePasswordUser(User user, String password) throws ServerException {
        DataBase.getInstance().updatePasswordUser(user,password);
    }
}
