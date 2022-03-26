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
 * 发型师表
 * </p>
 *
 * @author ${author}
 * @since 2022-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("hair_master")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HairMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发型师ID
     */
      @TableId(value = "hair_master_id", type = IdType.AUTO)
    private Long hairMasterId;

    /**
     * 发型师名称
     */
    private String hairMasterName;

    /**
     * 发型师登录名
     */
    private String hairMasterCode;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户类型，默认为普通发型师
     * @see com.hair.management.bean.enumerate.HairMasterType
     */
    private Integer type;

    /**
     * 状态
     * @see com.hair.management.bean.enumerate.HairMasterStatus
     */
    private Integer status;

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
