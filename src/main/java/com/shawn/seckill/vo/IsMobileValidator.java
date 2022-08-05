package com.shawn.seckill.vo;

import com.shawn.seckill.utils.ValidatorUtil;
import com.shawn.seckill.vaildator.IsMobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码校验规则
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        //获取到填的值
        required = constraintAnnotation.required();//是否是必填的
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //判断是否是必填
        if (required){  //必填校验
            return ValidatorUtil.isMobile(value);
        }else{  //如果不是必填
            if (StringUtils.isEmpty(value)){
                return true;
            }else{  //非必填校验
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
