package kr.co.demojar.bean;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Getter
public class PagingHelper {

    // 페이징 전역변수 - req param
    private int pageNum = 1;

    private int pageSize = 20;

    @Setter
    private String startDate = "";

    @Setter
    private String endDate = "";

    @Setter
    private String searchText = "";

    private boolean isPageable;

    private long total = 0;
    private final long firstPageNum = 1;
    private long lastPageNum = 1;

    private long pageGroupSize = 10;
    // 페이징 전역변수 - limit , offset
    private long limit = 10;
    private long offset = 0;
    // 페이징 전역변수 - 현재 페이지 그룹, 전체 페이지 그룹
    private long currentPageGroupCount = 1;
    private long totalPageGroupCount = 1;
    // 페이징 전역변수 - 페이징 그룹의 첫번째 페이지, 마지막 페이지
    private long firstPageNumOfPageGroup = 1;
    private long lastPageNumOfPageGroup = 1;
    // 페이징 전역변수
    private long prevGroupPageNum = 1;
    private long nextGroupPageNum = 1;

    private boolean prevPageExist = false;
    private boolean nextPageExist = false;

    private boolean notInFirstPage = false;
    private boolean notInLastPage = false;

    public List<Long> getPageNumList() {
        List<Long> list = new ArrayList<>();
        for (long i = firstPageNumOfPageGroup; i <= lastPageNumOfPageGroup; i++) {
            list.add(i);
        }
        return list;
    }

    public static PagingHelper init(Pageable pageable, long total) {

        PagingHelper pagingHelper = new PagingHelper();
        pagingHelper.setPageNum(pageable.getPageNumber() + 1);
        pagingHelper.setPageSize(pageable.getPageSize());
        pagingHelper.setTotal(total);
        pagingHelper.setPageable(true);

        return pagingHelper;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        calc();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        calc();
    }

    public void setPageable(boolean pageable) {
        isPageable = pageable;
        calc();
    }

    public void setTotal(long total) {
        this.total = total;
        calc();
    }

    public void setPageGroupSize(long pageGroupSize) {
        this.pageGroupSize = pageGroupSize;
        calc();
    }

    public void calc() {

        limit = pageSize;
        offset = (pageNum - 1) * pageSize;

        if (!isPageable) {
            limit = -1;
            offset = -1;
        }

        /**
         * 계산
         */
        lastPageNum = (                 //
                total % pageSize > 0                 //
                        ? (total / pageSize) + 1                 //
                        : total / pageSize                 //
        );

        currentPageGroupCount = (                 //
                pageNum % pageGroupSize > 0                 //
                        ? (pageNum / pageGroupSize) + 1                 //
                        : pageNum / pageGroupSize                 //
        );

        totalPageGroupCount = (                 //
                lastPageNum % pageGroupSize > 0                 //
                        ? (lastPageNum / pageGroupSize) + 1                 //
                        : lastPageNum / pageGroupSize                 //
        );

        firstPageNumOfPageGroup = (currentPageGroupCount - 1) * pageGroupSize + 1;
        lastPageNumOfPageGroup = firstPageNumOfPageGroup + pageGroupSize - 1;
        lastPageNumOfPageGroup = Math.min(lastPageNumOfPageGroup, lastPageNum);

        prevGroupPageNum = firstPageNumOfPageGroup - 1;
        nextGroupPageNum = lastPageNumOfPageGroup + 1;

        prevPageExist = (prevGroupPageNum >= 1);
        nextPageExist = (nextGroupPageNum <= lastPageNum);

        notInFirstPage = currentPageGroupCount > 1 && totalPageGroupCount > 1;
        notInLastPage = currentPageGroupCount != totalPageGroupCount && totalPageGroupCount > 1;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", PagingHelper.class.getSimpleName() + "[", "]")
                .add("pageNum=" + pageNum)
                .add("pageSize=" + pageSize)
                .add("startDate='" + startDate + "'")
                .add("endDate='" + endDate + "'")
                .add("searchText='" + searchText + "'")
                .add("isPageable=" + isPageable)
                .add("total=" + total)
                .add("firstPageNum=" + firstPageNum)
                .add("lastPageNum=" + lastPageNum)
                .add("pageGroupSize=" + pageGroupSize)
                .add("limit=" + limit)
                .add("offset=" + offset)
                .add("currentPageGroupCount=" + currentPageGroupCount)
                .add("totalPageGroupCount=" + totalPageGroupCount)
                .add("firstPageNumOfPageGroup=" + firstPageNumOfPageGroup)
                .add("lastPageNumOfPageGroup=" + lastPageNumOfPageGroup)
                .add("prevGroupPageNum=" + prevGroupPageNum)
                .add("nextGroupPageNum=" + nextGroupPageNum)
                .add("prevPageExist=" + prevPageExist)
                .add("nextPageExist=" + nextPageExist)
                .add("notInFirstPage=" + notInFirstPage)
                .add("notInLastPage=" + notInLastPage)
                .toString();
    }
}

