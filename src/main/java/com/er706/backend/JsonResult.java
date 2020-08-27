package com.er706.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class JsonResult<T> {

  @JsonInclude
  @ApiModelProperty("服务响应情况, ok/err")
  private String code;
  @JsonInclude(Include.NON_NULL)
  @ApiModelProperty("如果出错的话这里是具体的异常，field -> list of error")
  private Map<String, List<String>> errors;
  @JsonInclude(Include.NON_NULL)
  @ApiModelProperty("成功响应数据体（如果有）")
  private T data;

  protected JsonResult(String code, Map<String, List<String>> errors, T data) {
    this.code = code;
    this.errors = errors;
    this.data = data;
  }

  public static <T> JsonResult ok(T data) {
    return new JsonResult("ok", null, data);
  }

  public static JsonResult ok() {
    return new JsonResult("ok", null, null);
  }

  public static JsonResult err(Map<String, List<String>> errors) {
    return new JsonResult("err", errors, null);
  }

  public static JsonResult err(String field, String error) {
    Map<String, List<String>> errors = new HashMap<>();
    errors.put(field, Collections.singletonList(error));
    return new JsonResult("err", errors, null);
  }
}
