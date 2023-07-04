package kr.co.demojar.bean.dto;

import kr.co.demojar.bean.entity.BoardEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Calendar;

/**
 * DTO for {@link BoardEntity}
 */
@Getter
@Setter
public class BoardEntityDto implements Serializable {

    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Calendar createdAt;

    private Calendar updatedAt;

    @NotBlank
    private String writer;

}