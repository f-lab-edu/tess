package com.flab.tess.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseEntityTest {

    @Test
    public void testPrePersist() {
        BaseEntity entity = new BaseEntity() {};

        // PrePersist 직접 호출
        entity.createdAt();
        // createdAt() 호출 직후의 시간을 가져옴
        LocalDateTime localDateTime = entity.getCreatedAt();

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());

        assertTrue(entity.getCreatedAt().isEqual(localDateTime));
        assertTrue(entity.getUpdatedAt().isEqual(localDateTime));

    }

    @Test
    public void testPreUpdate() throws InterruptedException {
        BaseEntity entity = new BaseEntity() {};

        // PrePersist 메서드 직접 호출
        entity.createdAt();
        LocalDateTime updatedAt = entity.getUpdatedAt();

        Thread.sleep(1000); // 1초 대기

        // PreUpdate 메서드 직접 호출
        entity.updatedAt();

        assertNotNull(entity.getUpdatedAt());

        //업데이트된 시점이 이전의 updateAt보다 이후가 되니까 isAfter로 검증증
       assertTrue(entity.getUpdatedAt().isAfter(updatedAt));
    }

}
