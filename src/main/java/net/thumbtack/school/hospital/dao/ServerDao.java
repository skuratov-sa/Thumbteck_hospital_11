package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.db.DataBase;

public interface ServerDao {
    void startServer(DataBase dataBase);
    DataBase getDB();
    void startServer();
}
