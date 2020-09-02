package com.er706.backend.captcha;

public interface SmsService {

  String generateCode(String phoneNumber);

  boolean verifyCode(String phoneNumber, String code);
}
