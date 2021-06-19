package net.thumbtack.school.hospital.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.request.GetUserByTokenDtoRequest;
import net.thumbtack.school.hospital.request.LogInUserDtoRequest;
import net.thumbtack.school.hospital.request.UpdatePassUserDtoRequest;
import net.thumbtack.school.hospital.response.EmptyResponse;
import net.thumbtack.school.hospital.response.GetTokenDtoResponse;

import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.request.GetUserByTokenDtoRequest;
import net.thumbtack.school.hospital.request.LogInUserDtoRequest;
import net.thumbtack.school.hospital.request.UpdatePassUserDtoRequest;
import net.thumbtack.school.hospital.response.EmptyResponse;
import net.thumbtack.school.hospital.response.GetTokenDtoResponse;

import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.request.GetUserByTokenDtoRequest;
import net.thumbtack.school.hospital.request.LogInUserDtoRequest;
import net.thumbtack.school.hospital.request.UpdatePassUserDtoRequest;
import net.thumbtack.school.hospital.response.EmptyResponse;
import net.thumbtack.school.hospital.response.GetTokenDtoResponse;

import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.hospital.dao.UserDao;
import net.thumbtack.school.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.school.hospital.exeptions.AnswerErrorCode;
import net.thumbtack.school.hospital.exeptions.ServerException;
import net.thumbtack.school.hospital.response.ErrorResponse;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.request.GetUserByTokenDtoRequest;
import net.thumbtack.school.hospital.request.LogInUserDtoRequest;
import net.thumbtack.school.hospital.request.UpdatePassUserDtoRequest;
import net.thumbtack.school.hospital.response.EmptyResponse;
import net.thumbtack.school.hospital.response.GetTokenDtoResponse;

import java.util.regex.Pattern;

public class UserService {
    private final Gson gson = new Gson();
    private final UserDao userDao = new UserDaoImpl();

    public String logInUser(String jsonText) {
        try {
            LogInUserDtoRequest loginDto = getClassFromJson(jsonText, LogInUserDtoRequest.class);
            validateLogin(loginDto);

            return gson.toJson(new GetTokenDtoResponse(userDao.logInUser(loginDto.getLogin(), loginDto.getPassword())));
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateLogin(LogInUserDtoRequest loginDto) throws ServerException {
        if (!Pattern.matches("[A-Za-z0-9]{3,20}@[a-z]{2,6}[.][a-z]{2,4}", loginDto.getLogin())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_LOGIN);
        }
        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", loginDto.getPassword())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
    }


    public String logOutUser(String tokenJson) {
        try {
            GetUserByTokenDtoRequest logOutDto = getClassFromJson(tokenJson, GetUserByTokenDtoRequest.class);
            validateLogOutRequest(logOutDto);

            User user = userDao.getByToken(logOutDto.getToken());
            userDao.logOutUser(user);
            return gson.toJson(new EmptyResponse());
        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateLogOutRequest(GetUserByTokenDtoRequest logOutDto) throws ServerException {
        if (logOutDto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
    }

    public String updatePassUser(String jsonRequest) {
        try {
            UpdatePassUserDtoRequest updateDto = getClassFromJson(jsonRequest, UpdatePassUserDtoRequest.class);
            validateUpdatePasswordRequest(updateDto);

            User user = userDao.getByToken(updateDto.getToken());
            userDao.updatePasswordUser(user, updateDto.getPassword());
            return gson.toJson(new EmptyResponse());

        } catch (ServerException serverException) {
            return gson.toJson(new ErrorResponse(serverException.getErrorMessage()));
        }
    }

    private void validateUpdatePasswordRequest(UpdatePassUserDtoRequest dto) throws ServerException {
        if (dto.getToken() == null) {
            throw new ServerException(AnswerErrorCode.NULL_REQUEST);
        }
        if (!Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", dto.getPassword())) {
            throw new ServerException(AnswerErrorCode.REGISTRATION_WRONG_VOLIDATE_PASSWORD);
        }
    }


    public <T> T getClassFromJson(String jsonRequest, Class<T> tClass) throws ServerException {
        try {
            return gson.fromJson(jsonRequest, tClass);
        } catch (JsonSyntaxException e) {
            throw new ServerException(AnswerErrorCode.WRONG_JSON_SYNTAX);
        }
    }

}
