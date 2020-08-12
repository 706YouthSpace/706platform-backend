package com.er706.backend.index;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("index")
@Api("首页相关的请求")
public class IndexController {

  @Autowired
  IndexActivityDAO activityDAO;

  @GetMapping(value = "/activities")
  @Cacheable("index_activities")
  @ApiOperation("获取首页活动")
  public List<IndexActivity> getActivities() {
    return activityDAO.findAll(PageRequest.of(0, 4, Direction.DESC, "id")).getContent();
  }
}
