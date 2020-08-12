package com.er706.backend.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Entity(name = "index_activities")
@Data
@ApiModel(description = "首页活动数据")
public class IndexActivity {

  @Id
  @GeneratedValue
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  @ApiModelProperty("活动开始时间")
  private Date datetime;

  @Column(nullable = false)
  @ApiModelProperty("活动名称")
  private String name;

  @Column(nullable = false)
  @ApiModelProperty("活动背景图")
  private String image;
}
