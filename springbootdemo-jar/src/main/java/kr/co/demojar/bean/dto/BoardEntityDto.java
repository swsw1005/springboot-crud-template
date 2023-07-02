package kr.co.demojar.bean.dto;

import kr.co.demojar.bean.entity.BoardEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Calendar;

/**
 * DTO for {@link BoardEntity}
 */
@Getter
@Setter
public class BoardEntityDto implements Serializable {
    private String id;
    private String title;
    private String body;
    private Calendar createdAt;
    private String writer;
}