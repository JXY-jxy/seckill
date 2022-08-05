package com.shawn.seckill.vo;

import com.shawn.seckill.vaildator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author : shawn
 * @date : 2022-07-20 12:36
 * @description :
 **/
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 6)
    private String password;
}
