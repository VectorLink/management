package com.hair.management.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 发型师表
 * </p>
 *
 * @author ${author}
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("hair_master")
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
