package com.er706.backend.captcha;

import com.yunpian.sdk.YunpianClient;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"integration", "product"})
public class YunpianSmsService implements SmsService {

  private YunpianClient client;

  @Value("${yunpian.apikey}")
  private String API_KEY;

  @Autowired
  private CacheManager cacheManager;

  @PostConstruct

  protected void build() {
    client = new YunpianClient(API_KEY).init();
  }

  @Override
  public String generateCode(String phoneNumber) {
    Cache cache = cacheManager.getCache("sms");
    String phoneCode = RandomStringUtils.randomAlphanumeric(4);
    cache.put(phoneNumber, phoneCode); //这里要更新，如果之前有值的话就会被替代掉
    Map<String, String> param = client.newParam(2);
    param.put(YunpianClient.MOBILE, phoneNumber);
    param.put(YunpianClient.TEXT, String.format("【云片网】您的验证码是%s", phoneCode));
    client.sms().single_send(param);
    return phoneCode;
  }

  @Override
  public boolean verifyCode(String phoneNumber, String code) {
    Cache cache = cacheManager.getCache("sms");
    String phoneCode = cache.get(phoneNumber, String.class);
    if (code.equalsIgnoreCase(phoneCode)) {
      cache.evictIfPresent(phoneNumber);
      return true;
    } else {
      return false;
    }
  }

  @PreDestroy
  protected void shutdown() {
    client.close();
  }
}
