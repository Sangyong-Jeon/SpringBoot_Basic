package com.example.springboot_basic.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy // Entity 생성시 자동 저장
    @Column(name = "CREATED_NAME", updatable = false)
    private String createdBy;

    @LastModifiedBy // Entity 수정시 자동 저장
    @Column(name = "UPDATED_NAME")
    private String updatedBy;

}
/*
BaseTimeEntity와는 다르게 어떤 데이터를 기준으로 넣어줄 것인지 알려줘야한다.
따라서 AuditorAware를 스프링 빈으로 등록하여 이 인터페이스에 getCurrentAuditor() 메서드를 구현해주면 된다.
getCurrentAuditor() 함수에서 반환한 값이 기준이 된다.
SpringBootBasicApplication 클래스에 바로 등록해줘도 되지만 security를 사용하여 사용자를 저장할 것이기 때문에 config 패키지안에 클래스를 따로 만든다.
*/
/*
BaseEntity와 BaseTimeEntity를 따로 분리한 이유는 클래스마다 생성자, 수정자를 안쓰지만 생성시간, 수정시간은 많이 쓰기때문에 분리해서 적용하려고 한 것이다.
따라서 BaseEntity를 상속받으면 생성자, 수정자, 생성시간, 수정시간 컬럼이 생기고
BaseTimeEntity를 상속받으면 생성시간, 수정시간 컬럼만 생긴다.
 */
