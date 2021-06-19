package net.thumbtack.school.hospital.service;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.dao.ServerDao;
import net.thumbtack.school.hospital.daoimpl.ServerDaoImpl;
import net.thumbtack.school.hospital.db.DataBase;
import net.thumbtack.school.hospital.response.EmptyResponse;
import net.thumbtack.school.hospital.response.ErrorResponse;

import java.io.*;

public class ServerService {
    private final ServerDao serverDao = new ServerDaoImpl();
    private final Gson gson = new Gson();

    public String start(String savedDataFileName) {
        try {
            validateFileName(savedDataFileName);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedDataFileName));
            serverDao.startServer((DataBase) ois.readObject());
            ois.close();
            return gson.toJson(new EmptyResponse());
        } catch (IOException | ClassNotFoundException e) {
            serverDao.startServer();
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }


    public String stop(String serverService) {
        try {
            validateFileName(serverService);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serverService));
            DataBase db = serverDao.getDB();
            oos.writeObject(db);
            db.clear();
            oos.close();
            return gson.toJson(new EmptyResponse());

        } catch(IOException | ClassNotFoundException e){

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/java/net/thumbtack/school/hospital/fileDB/DataBase.txt"))) {
                    oos.writeObject(serverDao.getDB());
                } catch (IOException ignored) {}
                 return gson.toJson(new ErrorResponse(e.getMessage()));
            }
        }

    private void validateFileName(String fileName) throws ClassNotFoundException {
        if (fileName.length() == 0) {
            throw new ClassNotFoundException();
        }
    }
}
