package com.allchat.allchat.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @CreatedDate
    @Column(name="reg_date", updatable = false)
    private LocalDateTime regDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate;
}
