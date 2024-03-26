package com.networkKnights.ecms_auth.constant;

public class URIConstant {
    public static String SECRET = "3272357538782F413F4428472B4B6250655368566D5971337436763979244226";
    public static long EXPIRATION_TIME = 1000 * 60 * 60 * 10;
    public static final String URI_AUTH = "/auth";
    public static final String URI_UNAUTHORIZED = "/unauthorized";
    public static final String URI_LOGIN = "/login";
    public static final String URI_VALIDATE = "/validate";
    public static final String URI_CHECK_EMAIL = "/checkEmail";
    public static final String URI_PATH_VARIABLE_EMAIL = "/{email}";
    public static final String URI_VALIDATE_TOKEN = "/validateToken";
    public static final String URI_FORGOT_PASSWORD = "/forgotPassword";
    public static final String URI_SET_PASSWORD_WITH_OTP = "/setPasswordWithOtp";
    public static final String URI_VERIFY_OTP = "/verifyOtp";
    public static final String URI_EMAIL = "/email";
    public static final String URI_INFORM_SALARY_CHANGE = "/informSalaryChange";
}
