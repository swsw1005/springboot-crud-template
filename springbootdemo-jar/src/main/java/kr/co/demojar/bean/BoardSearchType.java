package kr.co.demojar.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BoardSearchType {
    title,
    body,
    writer;

    @JsonCreator
    public static BoardSearchType from(String value) {
        for (BoardSearchType boardSearchType : BoardSearchType.values()) {
            if (boardSearchType.name().equals(value)) {
                return boardSearchType;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name();
    }
}
