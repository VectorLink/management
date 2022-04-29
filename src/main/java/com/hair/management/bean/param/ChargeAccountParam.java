package com.hair.management.bean.param;

import com.hair.management.bean.enumerate.ConsumerType;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.io.File;
import java.math.BigDecimal;

/**
 * 变化账户金额类型
 */
@Data
@Builder
public class ChargeAccountParam {
    Integer consumerType;
    BigDecimal alterAmount;
    Long userId;
}
