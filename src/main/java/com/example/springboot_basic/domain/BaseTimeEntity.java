package com.example.springboot_basic.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 부모 클래스를 상속받는 자식 클래스에게 매핑 정보만 제공할 때 사용함. 즉 이 클래스는 Entity가 아니다.
// EntityListeners는 JPA Entity에서 이벤트 발생할 때 특정 로직을 실행시키는 어노테이션이다.
// 즉, AuditingEntityListener 클래스가 callback listener로 지정되어 Entity에서 이벤트가 발생할 때마다 특정 로직 수행함. Auditing 기능이 클래스에 적용된 것
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate // Entity가 생성되어 저장될 때 시간 자동 저장
    @Column(name = "CREATED_DATE", updatable = false) // 컬럼명 "CREATED_DATE", updatable=false은 update 시점에 정지, insertable=false은 insert 시점에 정지
    private LocalDateTime createdDate;

    @LastModifiedDate // Entity의 값을 변경할 때 시간 자동 저장
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

}
// JPA Auditing 기능을 활성화하려면 꼭 SpringBootBasicApplication 클래스에 @EnableJpaAuditing 적어주기