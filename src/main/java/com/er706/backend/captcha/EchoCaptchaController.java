package com.er706.backend.captcha;

import com.er706.backend.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/captcha")
@Profile("develop")
@Api("开发环境伪图形验证码服务，验证成功后走短信下发服务")
public class EchoCaptchaController {

  @Autowired
  SmsService smsService;

  @GetMapping("/register")
  @ApiOperation("获取伪图形验证码")
  public JsonResult<String> getCaptchaRegister(
      @ApiParam("手机号") @RequestParam("phoneNumber") String phoneNumber,
      @ApiIgnore HttpSession session) {
    String code = RandomStringUtils.randomAlphanumeric(5);
    session.setAttribute("phoneNumber", phoneNumber);
    session.setAttribute("captchaCode", code);
    return JsonResult.ok(code);
  }

  @PostMapping("/validate")
  @ApiImplicitParams(
      {
          @ApiImplicitParam(name = "code", value = "输入验证码", paramType = "body", required = true)
      }
  )
  @ApiOperation("验证伪图形验证码, 成功后发送短信验证码")
  public JsonResult<String> validateCaptcha(
      @ApiIgnore @RequestBody Map<String, String> body,
      @ApiIgnore HttpSession session) {
    String captchaCode = (String) session.getAttribute("captchaCode");
    if (body.get("code") != null && body.get("code").equals(captchaCode)) {
      String phoneNumber = (String) session.getAttribute("phoneNumber");
      session.removeAttribute("captchaCode");
      session.removeAttribute("phoneNumber");
      String phoneCode = smsService.generateCode(phoneNumber);
      return JsonResult.ok(phoneCode);
    } else {
      return JsonResult.err("captcha", "error");
    }
  }

}
