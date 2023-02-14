package com.liuyun.base.result;

import com.liuyun.base.exception.VerifyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ResultTests
 *
 * @author W.d
 * @since 2022/11/23 14:55
 **/
class ResultTests {

    @Test
    void verifyTest() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(VerifyException.class,
                        () -> Result.verify(Result.fail())),
                () -> Assertions.assertThrows(VerifyException.class,
                        () -> Result.verify(Result.fail(), "fail")),
                () -> Assertions.assertThrows(RuntimeException.class,
                        () -> Result.verify(Result.fail(), () -> new RuntimeException("fail")))
        );
    }

    @Test
    void dataIgnoreNullTest() {
        Assertions.assertNull(Result.dataIgnoreNull(Result.success()));
    }

    @Test
    void dataNotNullTest() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(Result.dataNotNull(Result.success("data"))),
                () -> Assertions.assertThrows(VerifyException.class, () -> Result.dataNotNull(Result.success()))
        );
    }
}
