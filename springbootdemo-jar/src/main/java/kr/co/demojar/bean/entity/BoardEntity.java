package kr.co.demojar.bean.entity;

import kr.co.demojar.bean.dto.BoardEntityDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.UUID;

@Getter
@Builder
@Entity(name = "board")
@Table(name = "tb_board", indexes = {@Index(name = "idx_writer", columnList = "writer"), @Index(name = "idx_created_at", columnList = "created_at"),})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity {

    @NotNull
    @NotEmpty
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @Builder.Default
    private String id = UUID.randomUUID().toString().toLowerCase();

    @NotNull
    @NotEmpty
    @Column(name = "title", length = 200, updatable = false, nullable = false)
    private String title;

    @Column(name = "body", length = 1500)
    private String body;

    @Column(name = "created_at", updatable = false, nullable = false)
    @Builder.Default
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt = Calendar.getInstance();

    @Column(name = "updated_at", updatable = true)
    @Builder.Default
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt = Calendar.getInstance();

    @NotNull
    @NotEmpty
    @Column(name = "writer", length = 100, updatable = false, nullable = false)
    private String writer;


    /**
     * https://mangkyu.tistory.com/174
     *
     * @param title
     * @param body
     */
    public void update(
            //
            @NotNull @NotEmpty final String title, //
            final String body) {
        this.title = title;
        this.body = body;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Calendar.getInstance();
    }
}
