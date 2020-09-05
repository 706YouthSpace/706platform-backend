package com.er706.backend.account;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.Account;
import com.er706.backend.captcha.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import springfox.documentation.annotations.ApiIgnore;

@RestController("/account")
@Api("账户信息相关接口")
public class AccountController {

  protected final static String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\n";
  protected final static String PHONE_PATTERN = "^1[3-9]\\d{9}$";
  Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
  Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
  @Autowired
  AccountDAO accountDAO;
  @Autowired
  SmsService smsService;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  CacheManager cacheManager;
  @Autowired
  JavaMailSender mailSender;
  @Autowired
  SpringTemplateEngine templateEngine;
  @Value("${spring.mail.username}")
  String mailFrom;

  @PostMapping("/email")
  @Secured("ROLE_USER")
  @ApiOperation("修改邮箱接口，提交后下发邮件")
  public JsonResult changeEmail(@ApiIgnore Authentication currentUser,
      @Valid @RequestBody ChangeEmailInput changeEmailInput) {
    Account account = (Account) currentUser.getPrincipal();
    Long uid = account.getUid();
    String uuid = UUID.randomUUID().toString();
    Cache cache = cacheManager.getCache("account.email");
    String cacheKey = String
        .format("%s@%s", uid, changeEmailInput.getEmail());
    cache.put(cacheKey, uuid);
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

    try {
      message.setFrom(mailFrom);
      message.setTo(changeEmailInput.getEmail());
      message.setSubject("邮箱验证");
      Context context = new Context();
      context
          .setVariables(Map.of("email", changeEmailInput.getEmail(), "uid", uid, "code", uuid));
      String htmlBody = templateEngine.process("mail", context);
      message.setText(htmlBody, true);
      mailSender.send(mimeMessage);
      return JsonResult.ok();
    } catch (MessagingException e) {
      return JsonResult.err("mail", e.getMessage());
    }
  }

  @PostMapping("/email/verification")
  @Transactional
  @ApiOperation("邮箱验证接口")
  public JsonResult emailVerify(@Valid @RequestBody EmailVerificationInput emailVerificationInput) {
    Cache cache = cacheManager.getCache("account.email");
    //verify code
    String cacheKey = String
        .format("%s@%s", emailVerificationInput.getUid(), emailVerificationInput.getEmail());
    String code = cache.get(cacheKey, String.class);
    //这里做严格匹配，因为不需要用户手动输入
    if (code.equals(emailVerificationInput.getVerificationCode())) {
      //把自己之前的邮箱账号找出来
      List<AccountModel> accounts = accountDAO.findAllByUid(emailVerificationInput.getUid());
      Optional<AccountModel> otherAccount = accounts.stream()
          .filter(accountModel -> !emailPattern.matcher(accountModel.getAccount()).matches())
          .filter(accountModel -> accountModel.getPassword().length() > 0)
          .findFirst();
      Optional<AccountModel> emailAccount = accounts.stream()
          .filter(accountModel -> emailPattern.matcher(accountModel.getAccount()).matches())
          .filter(accountModel -> accountModel.getPassword().length() > 0)
          .findFirst();
      //总得至少有一个带密码的账号
      assert emailAccount.isPresent() || otherAccount.isPresent();
      AccountModel account = emailAccount.isPresent() ? emailAccount.get() : otherAccount.get();
      if (emailAccount.isPresent()) {
        //如果之前已经有邮箱，那么就把之前的邮箱账号删除
        accountDAO.delete(emailAccount.get());
      }
      //新邮箱看看在数据库里有没有
      Optional<AccountModel> newEmail = accountDAO.findByAccount(emailVerificationInput.getEmail());
      if (newEmail.isPresent()) { //新邮箱如果有的话就直接删除，不做修改的原因是原账号还有密码，还有可能id被潜在用在别的地方的可能性，带来安全问题
        accountDAO.delete(newEmail.get());
      }
      //然后再用新邮箱创建个新的账号
      AccountModel newAccount = AccountModel.builder().uid(account.getUid()).account(
          emailVerificationInput.getEmail()).password(account.getPassword()).build();
      accountDAO.save(newAccount);
      cache.evict(cacheKey);
      return JsonResult.ok();
    } else {
      return JsonResult.err("code", "error");
    }

  }

  @GetMapping("/email")
  @Secured("ROLE_USER")
  @ApiOperation("查看当前邮箱")
  public JsonResult email(@ApiIgnore Authentication currentUser) {
    Account account = (Account) currentUser.getPrincipal();
    Long uid = account.getUid();
    List<AccountModel> accounts = accountDAO.findAllByUid(uid);
    Optional<AccountModel> emailAccount = accounts.stream()
        .filter(accountModel -> emailPattern.matcher(accountModel.getAccount()).matches())
        .findFirst();
    return JsonResult.ok(emailAccount.get());
  }

  @PostMapping("/phone")
  @Secured("ROLE_USER")
  @ApiOperation("修改手机号")
  public JsonResult changePhone(@ApiIgnore Authentication currentUser,
      @Valid @RequestBody ChangePhoneInput changePhoneInput) {
    Account account = (Account) currentUser.getPrincipal();
    Long uid = account.getUid();
    String code = changePhoneInput.getCode();
    String phoneNumber = changePhoneInput.getPhoneNumber();
    if (smsService.verifyCode(phoneNumber, code)) {
      //把自己之前的手机账号找出来
      List<AccountModel> accounts = accountDAO.findAllByUid(uid);
      Optional<AccountModel> otherAccount = accounts.stream()
          .filter(accountModel -> !phonePattern.matcher(accountModel.getAccount()).matches())
          .filter(accountModel -> accountModel.getPassword().length() > 0)
          .findFirst();
      Optional<AccountModel> phoneAccount = accounts.stream()
          .filter(accountModel -> phonePattern.matcher(accountModel.getAccount()).matches())
          .filter(accountModel -> accountModel.getPassword().length() > 0)
          .findFirst();
      //总得至少有一个带密码的账号
      assert phoneAccount.isPresent() || otherAccount.isPresent();
      //为什么再从数据库查一下？因为如果直接使用登陆的账号的话，有可能没有密码（第三方登陆）
      AccountModel oldAccount = phoneAccount.isPresent() ? phoneAccount.get() : otherAccount.get();
      if (phoneAccount.isPresent()) {
        //如果之前已经有手机账号，那么就把之前的邮箱账号删除
        accountDAO.delete(phoneAccount.get());
      }
      //新手机看看在数据库里有没有
      Optional<AccountModel> newPhone = accountDAO.findByAccount(phoneNumber);
      if (newPhone.isPresent()) { //新邮箱如果有的话就直接删除，不做修改的原因是原账号还有密码，还有可能id被潜在用在别的地方的可能性，带来安全问题
        accountDAO.delete(newPhone.get());
      }
      //然后再用新手机创建个新的账号
      AccountModel newAccount = AccountModel.builder().uid(uid).account(
          phoneNumber).password(oldAccount.getPassword()).build();
      accountDAO.save(newAccount);
      return JsonResult.ok();
    } else {
      return JsonResult.err("code", "error");
    }
  }

  @GetMapping("/phone")
  @Secured("ROLE_USER")
  @ApiOperation("查看当前手机号")
  public JsonResult phone(@ApiIgnore Authentication currentUser) {
    Account account = (Account) currentUser.getPrincipal();
    Long uid = account.getUid();
    List<AccountModel> accounts = accountDAO.findAllByUid(uid);
    Optional<AccountModel> phoneAccount = accounts.stream()
        .filter(accountModel -> phonePattern.matcher(accountModel.getAccount()).matches())
        .findFirst();
    return JsonResult.ok(phoneAccount.get());
  }

  @PostMapping("/password")
  @Secured("ROLE_USER")
  @ApiOperation("修改密码")
  public JsonResult changePassword(@ApiIgnore Authentication currentUser,
      @Valid @RequestBody ChangePasswordInput changePasswordInput) {
    Account account = (Account) currentUser.getPrincipal();
    Long uid = account.getUid();
    List<AccountModel> accounts = accountDAO.findAllByUid(uid);
    List<AccountModel> accountsWithPassword = accounts.stream()
        .filter(accountModel -> accountModel.getPassword().length() > 0)
        .collect(Collectors.toList());
    accountsWithPassword.forEach(accountModel -> accountModel
        .setPassword(passwordEncoder.encode(changePasswordInput.getPassword())));
    accountDAO.saveAll(accountsWithPassword);
    return JsonResult.ok();
  }
}
