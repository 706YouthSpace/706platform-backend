package com.er706.backend.auth;

import com.er706.backend.JsonResult;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Component
@ApiIgnore
public class DefaultErrorController extends BasicErrorController {

  public DefaultErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes, new ErrorProperties());
  }

  @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE})
  @ResponseBody
  public JsonResult jsonError() {
    return JsonResult.err("authentication", "unauthorized");
  }
}
