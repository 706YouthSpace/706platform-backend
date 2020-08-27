package com.er706.backend.captcha;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("develop")
public class EchoSmsService implements SmsService {

  @Autowired
  private CacheManager cacheManager;

  @Override
  public String generateCode(String phoneNumber) {
    Cache cache = cacheManager.getCache("sms.echo");
    String phoneCode = RandomStringUtils.randomAlphanumeric(4);
    cache.put(phoneNumber, phoneCode); //这里要更新，如果之前有值的话就会被替代掉
    return phoneCode;
  }

  @Override
  public boolean verifyCode(String phoneNumber, String code) {
    Cache cache = cacheManager.getCache("sms.echo");
    String phoneCode = cache.get(phoneNumber, String.class);
    if (code.equalsIgnoreCase(phoneCode)) {
      cache.evictIfPresent(phoneNumber);
      return true;
    } else {
      return false;
    }
  }
}
