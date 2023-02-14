package com.liuyun.base.utils;

import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * BigDecimal 工具类
 *
 * @author W.D
 * @since  2022/2/16 9:29 AM
 **/
public class BigDecimalUtil {

    /**
     * 默认小数长度
     */
    private static final Integer DEFAULT_SCALE = 2;

    /**
     * 默认舍入模式
     */
    private static final RoundingMode DEFAULT_MODE = RoundingMode.HALF_UP;

    /**
     * 加
     *
     * @param var1 {@link Object} var1
     * @param var2 {@link Object} var2
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:33 AM
     **/
    public static BigDecimal add(Object var1, Object var2) {
        return add(var1, var2, null, null);
    }

    /**
     * 加
     *
     * @param var1  {@link Object} var1
     * @param var2  {@link Object} var2
     * @param scale {@link Integer} 精度
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:33 AM
     **/
    public static BigDecimal add(Object var1, Object var2, Integer scale) {
        return add(var1, var2, scale, null);
    }

    /**
     * 加
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:33 AM
     **/
    public static BigDecimal add(Object var1, Object var2, RoundingMode roundingMode) {
        return add(var1, var2, null, roundingMode);
    }

    /**
     * 加
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param scale        {@link Integer} 精度
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal add(Object var1, Object var2, Integer scale, RoundingMode roundingMode) {
        if (containsNull(var1)) {
            return toBigDecimal(var2);
        }
        if (containsNull(var2)) {
            return toBigDecimal(var1);
        }
        BigDecimal var3 = toBigDecimal(var1);
        BigDecimal var4 = toBigDecimal(var2);
        if (isZero(var3)) {
            return var4;
        }
        if (isZero(var4)) {
            return var3;
        }
        scale = Optional.ofNullable(scale).orElse(DEFAULT_SCALE);
        roundingMode = Optional.ofNullable(roundingMode).orElse(DEFAULT_MODE);
        return var3.add(var4).setScale(scale, roundingMode);
    }

    /**
     * 减
     *
     * @param var1 {@link Object} var1
     * @param var2 {@link Object} var2
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal subtract(Object var1, Object var2) {
        return subtract(var1, var2, null, null);
    }

    /**
     * 减
     *
     * @param var1  {@link Object} var1
     * @param var2  {@link Object} var2
     * @param scale {@link Integer} 精度
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal subtract(Object var1, Object var2, Integer scale) {
        return subtract(var1, var2, scale, null);
    }

    /**
     * 减
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal subtract(Object var1, Object var2, RoundingMode roundingMode) {
        return subtract(var1, var2, null, roundingMode);
    }

    /**
     * 减
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param scale        {@link Integer} 精度
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal subtract(Object var1, Object var2, Integer scale, RoundingMode roundingMode) {
        if (allIsNull(var1, var2)) {
            return BigDecimal.ZERO;
        }
        BigDecimal var3 = toBigDecimal(var1);
        BigDecimal var4 = toBigDecimal(var2);
        scale = Optional.ofNullable(scale).orElse(DEFAULT_SCALE);
        roundingMode = Optional.ofNullable(roundingMode).orElse(DEFAULT_MODE);
        if (isZero(var3)) {
            // 取反
            return var4.negate().setScale(scale, roundingMode);
        }
        if (isZero(var4)) {
            return var3;
        }
        return var3.subtract(var4).setScale(scale, roundingMode);
    }

    /**
     * 乘
     *
     * @param var1 {@link Object} var1
     * @param var2 {@link Object} var2
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal multiply(Object var1, Object var2) {
        return multiply(var1, var2, null, null);
    }

    /**
     * 乘
     *
     * @param var1  {@link Object} var1
     * @param var2  {@link Object} var2
     * @param scale {@link Integer} 精度
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal multiply(Object var1, Object var2, Integer scale) {
        return multiply(var1, var2, scale, null);
    }

    /**
     * 乘
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal multiply(Object var1, Object var2, RoundingMode roundingMode) {
        return multiply(var1, var2, null, roundingMode);
    }

    /**
     * 乘
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param scale        {@link Integer} 精度
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal multiply(Object var1, Object var2, Integer scale, RoundingMode roundingMode) {
        // 只要其中一个 为 NULL 或者为空 直接返回 0
        if (containsNull(var1, var2)) {
            return BigDecimal.ZERO;
        }
        BigDecimal var3 = toBigDecimal(var1);
        BigDecimal var4 = toBigDecimal(var2);
        // 其中一个为 0 直接返回 0
        if (containsZero(var3, var4)) {
            return BigDecimal.ZERO;
        }
        scale = Optional.ofNullable(scale).orElse(DEFAULT_SCALE);
        roundingMode = Optional.ofNullable(roundingMode).orElse(DEFAULT_MODE);
        return var3.multiply(var4).setScale(scale, roundingMode);
    }

    /**
     * 除
     *
     * @param var1 {@link Object} var1
     * @param var2 {@link Object} var2
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal divide(Object var1, Object var2) {
        return divide(var1, var2, null, null);
    }

    /**
     * 除
     *
     * @param var1  {@link Object} var1
     * @param var2  {@link Object} var2
     * @param scale {@link Integer} 精度
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal divide(Object var1, Object var2, Integer scale) {
        return divide(var1, var2, scale, null);
    }

    /**
     * 除
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param roundingMode {@link RoundingMode} 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal divide(Object var1, Object var2, RoundingMode roundingMode) {
        return divide(var1, var2, null, roundingMode);
    }

    /**
     * 除
     *
     * @param var1         {@link Object} var1
     * @param var2         {@link Object} var2
     * @param scale        {@link Integer} 精度
     * @param roundingMode {@link } 舍入法
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:46 AM
     **/
    public static BigDecimal divide(Object var1, Object var2, Integer scale, RoundingMode roundingMode) {
        // 其中一个为 NULL 直接返回 0
        if (containsNull(var1, var2)) {
            return BigDecimal.ZERO;
        }
        BigDecimal var3 = toBigDecimal(var1);
        BigDecimal var4 = toBigDecimal(var2);
        // 其中一个为 0 直接返回 0
        if (containsZero(var3, var4)) {
            return BigDecimal.ZERO;
        }
        scale = Optional.ofNullable(scale).orElse(DEFAULT_SCALE);
        roundingMode = Optional.ofNullable(roundingMode).orElse(DEFAULT_MODE);
        return var3.divide(var4, scale, roundingMode);
    }

    /**
     * 大于等于 0
     *
     * @param number {@link BigDecimal}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 9:37 AM
     **/
    public static boolean geZero(BigDecimal number) {
        return gtZero(number) || isZero(number);
    }

    /**
     * 大于 0
     *
     * @param number {@link BigDecimal}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 9:37 AM
     **/
    public static boolean gtZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) < 0;
    }

    /**
     * 小于等于 0
     *
     * @param number {@link BigDecimal}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 9:37 AM
     **/
    public static boolean leZero(BigDecimal number) {
        return ltZero(number) || isZero(number);
    }

    /**
     * 小于 0
     *
     * @param number {@link BigDecimal}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 9:37 AM
     **/
    public static boolean ltZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) > 0;
    }

    /**
     * 是否为零
     *
     * @param number {@link BigDecimal}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 9:37 AM
     **/
    public static boolean isZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) == 0;
    }

    /**
     * 是否包含 null 的参数
     *
     * @param objs {@link Object[]}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 11:04 AM
     **/
    public static boolean containsNull(Object... objs) {
        return Stream.of(objs)
                .anyMatch(ObjectUtil::isNull);
    }

    /**
     * 全部为 null
     *
     * @param objs {@link Object[]}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 11:04 AM
     **/
    public static boolean allIsNull(Object... objs) {
        return Stream.of(objs).allMatch(ObjectUtil::isNull);
    }

    /**
     * 是否包含 0
     *
     * @param numbers {@link BigDecimal[]}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 11:04 AM
     **/
    public static boolean containsZero(BigDecimal... numbers) {
        return Stream.of(numbers)
                .anyMatch(number -> (BigDecimal.ZERO.compareTo(number) == 0));
    }

    /**
     * 全部为 0
     *
     * @param numbers {@link BigDecimal[]}
     * @return boolean
     * @author W.d
     * @since 2022/2/16 11:04 AM
     **/
    public static boolean allIsZero(BigDecimal... numbers) {
        return Stream.of(numbers)
                .allMatch(number -> (BigDecimal.ZERO.compareTo(number) == 0));
    }

    /**
     * Object 转 BigDecimal
     *
     * @param number {@link Object}
     * @return java.math.BigDecimal
     * @author W.d
     * @since 2022/2/16 9:33 AM
     **/
    public static BigDecimal toBigDecimal(Object number) {
        if (Objects.isNull(number)) {
            return BigDecimal.ZERO;
        }
        if (number instanceof Integer intValue) {
            return new BigDecimal(intValue);
        }
        if (number instanceof Long longValue) {
            return new BigDecimal(longValue);
        }
        if (number instanceof BigInteger bigIntValue) {
            return new BigDecimal(bigIntValue);
        }
        if (number instanceof BigDecimal bigDecimalValue) {
            return bigDecimalValue;
        }
        // Float、Double等有精度问题，转换为字符串后再转换
        return new BigDecimal(number.toString());
    }

    public static void main(String[] args) {
        System.out.println(add("1,1234.00", 1000L));
    }
}
