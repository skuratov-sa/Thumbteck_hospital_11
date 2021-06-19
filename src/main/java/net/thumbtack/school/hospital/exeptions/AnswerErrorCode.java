package net.thumbtack.school.hospital.exeptions;

public enum AnswerErrorCode {
    REGISTRATION_WRONG_VOLIDATE_PERSONAL_DATA("User personal data error!"),
    REGISTRATION_WRONG_VOLIDATE_LOGIN("Login error!"),
    REGISTRATION_WRONG_VOLIDATE_PASSWORD("Password error!"),
    REGISTRATION_THIS_USER_HAS_ALREADY_REGISTERED("Error, this user has already been registered!"),

    LOGIN_FAILED_INCORRECT_LOGIN_OR_PASSWORD("Wrong login or password!"),
    ID_ERROR("Id failed, user is not id in!"),

    NULL_REQUEST("The request contains a null field"),
    LOGIN_ERROR("Login failed, user is not login in!"),
    TOKEN_ERROR("Token failed, user is not token in!"),
    PASSWORD_ERROR("Password failed, user is not password in!"),

    WRONG_JSON_SYNTAX("The Json response has a syntax error"),
    ERROR_SPECIALITY("This specialty does not exist in the database"),

    ERROR_DOCTOR_DID_NOT_PATIENT("The doctor did not register the patient"),

    ERROR_DELETE_DOCTOR("You can't remove a doctor because there are no other doctors to transfer patients to"),
    PATIENT_ID_ERROR("There is no patient with this id"),

    ERROR_DRUG("There is no such drug"),
    NAME_FILE_ERROR("Could not find a file with the same name");

    private String msg;

    AnswerErrorCode(String msg) {
        setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if(msg.length() != 0) {
            this.msg = msg;
        }
    }
}
