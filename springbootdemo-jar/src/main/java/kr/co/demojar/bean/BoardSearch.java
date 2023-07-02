package kr.co.demojar.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * <PRE>
 * 게시판검색을 위한 검색객체
 * </PRE>
 */
@Getter
@Setter
public class BoardSearch {

    private int pn = 1;
    private int ps = 20;

    private String sd = "";
    private String ed = "";

    private String t = "";
    private String k = "";

    private int total = 0;


    @Override
    public String toString() {
        return new StringJoiner(", ", BoardSearch.class.getSimpleName() + "[", "]")
                .add("pn=" + pn)
                .add("ps=" + ps)
                .add("sd='" + sd + "'")
                .add("ed='" + ed + "'")
                .add("t=" + t)
                .add("k='" + k + "'")
                .add("total=" + total)
                .toString();
    }
}
