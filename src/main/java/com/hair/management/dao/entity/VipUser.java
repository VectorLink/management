package com.hair.management.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vip_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VipUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
      @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 发型师ID
     */
    private Long hairMasterId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


}
