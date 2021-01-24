package com.er706.backend.profile;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.Account;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@RequestMapping("/profile")
public class ProfileController {

  @Autowired
  ProfileDAO profileDAO;
  @Autowired
  EducationDAO educationDAO;
  @Autowired
  JobDAO jobDAO;
  @Autowired
  SocialAccountDAO socialAccountDAO;

  @GetMapping("/me")
  @Secured("ROLE_USER")
  @ApiOperation("取自己的个人信息")
  public JsonResult<Profile> me(@ApiIgnore Authentication currentUser) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profile = profileDAO.findById(account.getUid());
    return JsonResult.ok(profile.get());
  }

  @GetMapping("/{id}")
  @ApiOperation("取他人个人信息")
  public JsonResult<Profile> profile(@PathVariable Long id) {
    Optional<Profile> byId = profileDAO.findById(id);
    return JsonResult.ok(byId.get());
  }

  @PutMapping()
  @ApiOperation("更新个人信息，只更新传过来的字段")
  @Secured("ROLE_USER")
  public JsonResult<Profile> updateProfile(
      @ApiIgnore Authentication currentUser, @RequestBody @Validated ProfileForm profileForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    for (Field field : ProfileForm.class.getDeclaredFields()) {
      String fieldName = field.getName();
      try {
        field.setAccessible(true);
        Object value = field.get(profileForm);
        if (value != null) {
          Field profileField = Profile.class.getDeclaredField(fieldName);
          profileField.setAccessible(true);
          profileField.set(profile, value);
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {

      }

    }
    profileDAO.save(profile);
    return JsonResult.ok(profile);
  }

  @PostMapping("/job")
  @ApiOperation("更新个人信息，增加工作经历")
  @Secured("ROLE_USER")
  public JsonResult<Job> addJob(@ApiIgnore Authentication currentUser,
      @RequestBody @Validated JobForm jobForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    List<Job> jobs = profile.getJobs();
    Job newJob = new Job();
        newJob.setCompany(jobForm.getCompany());
    newJob.setTitle(jobForm.getTitle());
    newJob.setDescribtion(jobForm.getDescribtion());
    jobs.add(newJob);
    profileDAO.save(profile);
    return JsonResult.ok();
  }

  @PutMapping("/jobs/{id}")
  @ApiOperation("更新个人信息，更新工作经历")
  @Secured("ROLE_USER")
  public JsonResult<Job> updateJob(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long jobId,
      @RequestBody @Validated JobForm jobForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    for (Job job : profile.getJobs()) {
      if (job.getId() == jobId) {
        if (jobForm.getCompany() != null) {
          job.setCompany(jobForm.getCompany());
        }
        if (jobForm.getTitle() != null) {
          job.setTitle(jobForm.getTitle());
        }
        if (jobForm.getDescribtion() != null) {
          job.setDescribtion(jobForm.getDescribtion());
        }
        jobDAO.save(job);

        return JsonResult.ok(job);
      }
    }
    return JsonResult.err("id", "not.exists");
  }

  @DeleteMapping("/jobs/{id}")
  @ApiOperation("更新个人信息，删除工作经历")
  @Secured("ROLE_USER")
  public JsonResult<Job> delJob(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long jobId) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    Iterator<Job> iter = profile.getJobs().iterator();
    while (iter.hasNext()) {
      Job job = iter.next();
      if (job.getId() == jobId) {
        iter.remove();
        break;
      }
    }
    profileDAO.save(profile);
    return JsonResult.ok();
  }

  @PostMapping("/education")
  @ApiOperation("更新个人信息，增加教育经历")
  @Secured("ROLE_USER")
  public JsonResult<Education> addEducation(@ApiIgnore Authentication currentUser,
      @RequestBody @Validated EducationForm educationForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    List<Education> educations = profile.getEducations();
    Education newEducation = new Education();
    newEducation.setBegin(educationForm.getBegin());
    newEducation.setDescribtion(educationForm.getDescribtion());
    newEducation.setEnd(educationForm.getEnd());
    newEducation.setMajor(educationForm.getMajor());
    newEducation.setSchool(educationForm.getSchool());
    educations.add(newEducation);
    profileDAO.save(profile);
    return JsonResult.ok();
  }

  @PutMapping("/educations/{id}")
  @ApiOperation("更新个人信息，更新教育经历")
  @Secured("ROLE_USER")
  public JsonResult<Job> updateEducation(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long educationId,
      @RequestBody @Validated EducationForm educationForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    for (Education education : profile.getEducations()) {
      if (education.getId() == educationId) {

        if (educationForm.getBegin() != null) {
          education.setBegin(educationForm.getBegin());
        }
        if (educationForm.getDescribtion() != null) {
          education.setDescribtion(educationForm.getDescribtion());
        }
        if (educationForm.getEnd() != null) {
          education.setEnd(educationForm.getEnd());
        }
        if (educationForm.getMajor() != null) {
          education.setMajor(educationForm.getMajor());
        }
        if (educationForm.getSchool() != null) {
          education.setSchool(educationForm.getSchool());
        }
        educationDAO.save(education);
        return JsonResult.ok(education);
      }
    }
    return JsonResult.err("id", "not.exists");
  }

  @DeleteMapping("/educations/{id}")
  @ApiOperation("更新个人信息，删除教育经历")
  @Secured("ROLE_USER")
  public JsonResult<Job> delEducation(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long educationId) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    Iterator<Education> iter = profile.getEducations().iterator();
    while (iter.hasNext()) {
      Education education = iter.next();
      if (education.getId() == educationId) {
        iter.remove();
        break;
      }
    }
    profileDAO.save(profile);
    return JsonResult.ok();
  }

  @PostMapping("/social")
  @ApiOperation("更新个人信息，增加社交账号")
  @Secured("ROLE_USER")
  public JsonResult<SocialAccount> addSocialAccount(@ApiIgnore Authentication currentUser,
      @RequestBody @Validated SocialAccountForm socialAccountForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    List<SocialAccount> socialAccounts = profile.getSocials();
    SocialAccount newSocialAccount = new SocialAccount();
    newSocialAccount.setAccount(socialAccountForm.getAccount());
    newSocialAccount.setPlatform(socialAccountForm.getPlatform());
    newSocialAccount.setPrivacy(socialAccountForm.getPrivacy());
    newSocialAccount.setProfile(profile);
    socialAccounts.add(newSocialAccount);
    profileDAO.save(profile);
    return JsonResult.ok();
  }

  @PutMapping("/socials/{id}")
  @ApiOperation("更新个人信息，更新社交账号")
  @Secured("ROLE_USER")
  public JsonResult<Job> updateSocial(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long socialAccountId,
      @RequestBody @Validated SocialAccountForm socialAccountForm) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    for (SocialAccount socialAccount : profile.getSocials()) {
      if (socialAccount.getId() == socialAccountId) {
        if (socialAccountForm.getAccount() != null) {
          socialAccount.setAccount(socialAccountForm.getAccount());
        }
        if (socialAccountForm.getPlatform() != null) {
          socialAccount.setPlatform(socialAccountForm.getPlatform());
        }
        if (socialAccountForm.getPrivacy() != null) {
          socialAccount.setPrivacy(socialAccountForm.getPrivacy());
        }
        socialAccountDAO.save(socialAccount);
        return JsonResult.ok(socialAccount);
      }
    }
    return JsonResult.err("id", "not.exists");
  }

  @DeleteMapping("/socials/{id}")
  @ApiOperation("更新个人信息，删除社交账号")
  @Secured("ROLE_USER")
  public JsonResult<Job> delSocial(@ApiIgnore Authentication currentUser,
      @PathVariable("id") Long socialAccountId) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profileOpt = profileDAO.findById(account.getUid());
    Profile profile = profileOpt.get();
    Iterator<SocialAccount> iter = profile.getSocials().iterator();
    while (iter.hasNext()) {
      SocialAccount socialAccount = iter.next();
      if (socialAccount.getId() == socialAccountId) {
        iter.remove();
        break;
      }
    }
    profileDAO.save(profile);
    return JsonResult.ok();
  }

}
