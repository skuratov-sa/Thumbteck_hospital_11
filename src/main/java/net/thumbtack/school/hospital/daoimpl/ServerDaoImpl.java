package net.thumbtack.school.hospital.daoimpl;

import net.thumbtack.school.hospital.dao.ServerDao;
import net.thumbtack.school.hospital.db.DataBase;

public class ServerDaoImpl implements ServerDao {
    @Override
    public void startServer(DataBase dataBase) {
        DataBase.startServer(dataBase);
    }

    @Override
    public DataBase getDB() {
        return DataBase.stopServer();
    }

    @Override
    public void startServer() {
        DataBase.getInstance();
    }

}
