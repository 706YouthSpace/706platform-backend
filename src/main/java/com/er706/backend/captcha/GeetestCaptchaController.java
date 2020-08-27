package com.er706.backend.captcha;

import com.er706.backend.JsonResult;
import com.er706.backend.captcha.geetest.GeetestLib;
import com.er706.backend.captcha.geetest.GeetestLibResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Profile({"integration", "product"})
@Api("集成测试环境与生产环境的人机验证服务，验证成功后走短信下发")
public class GeetestCaptchaController {

  @Autowired
  SmsService smsService;

  @Value("${geetest.id}")
  String geetestId;

  @Value("${geetest.key}")
  String geetestKey;

  private String getIpAddr(HttpServletRequest request) {
    if (request.getHeader("X-Forwarded-For") != null) {
      return request.getHeader("X-Forwarded-For");
    } else {
      return request.getRemoteAddr();
    }

  }

  @GetMapping("/register")
  @ApiOperation("获取验证码参数")
  public JsonResult<String> getCaptchaRegister(@RequestParam("phoneNumber") String phoneNumber,
      @ApiIgnore HttpSession session, @ApiIgnore HttpServletRequest request) {

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("digestmod", "md5");
    paramMap.put("client_type", "web");
    paramMap.put("ip_address", getIpAddr(request));
    GeetestLib geetest = new GeetestLib(geetestId, geetestKey);
    GeetestLibResult result = geetest.register("md5", paramMap);
    session.setAttribute(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY, result.getStatus());
    session.setAttribute("phoneNumber", phoneNumber);
    return JsonResult.ok(result.getData());
  }

  @PostMapping("/validate")
  @ApiImplicitParams(
      {
          @ApiImplicitParam(name = "challenge", value = "极验参数", paramType = "body"),
          @ApiImplicitParam(name = "validate", value = "极验参数", paramType = "body"),
          @ApiImplicitParam(name = "seccode", value = "极验参数", paramType = "body"),
      }
  )
  @ApiOperation("验证Geetest验证码,发送短信验证码")
  public JsonResult validateCaptcha(
      @ApiIgnore @RequestBody Map<String, String> body,
      @ApiIgnore HttpSession session, @ApiIgnore HttpServletRequest request) {
    String challenge = body.get("challenge");
    String validate = body.get("validate");
    String seccode = body.get("seccode");
    GeetestLib geetest = new GeetestLib(geetestId, geetestKey);
    int status;
    try {
      status = (Integer) session
          .getAttribute(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY);
    } catch (Exception e) {
      return JsonResult.err("geetest", "error");
    }
    GeetestLibResult result = null;
    if (status == 1) {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put("client_type", "web");
      paramMap.put("ip_address", getIpAddr(request));
      result = geetest.successValidate(challenge, validate, seccode, paramMap);
    } else {
      result = geetest.failValidate(challenge, validate, seccode);
    }
    if (result.getStatus() == 1) {
      String phoneNumber = (String) session.getAttribute("phoneNumber");
      session.removeAttribute("phoneNumber");
      session.removeAttribute(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY);
      smsService.generateCode(phoneNumber);
      return JsonResult.ok();
    } else {
      return JsonResult.err("captcha", "error");
    }
  }
}
