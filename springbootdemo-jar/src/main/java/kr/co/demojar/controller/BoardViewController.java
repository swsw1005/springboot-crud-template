package kr.co.demojar.controller;

import kr.co.demojar.bean.BoardSearch;
import kr.co.demojar.bean.PagingHelper;
import kr.co.demojar.bean.dto.BoardEntityDto;
import kr.co.demojar.exception.NoContentException;
import kr.co.demojar.service.BoardReadOnlyService;
import kr.co.demojar.bean.BoardSearchType;
import kr.co.demojar.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardViewController {

    private final BoardService boardService;

    private final BoardReadOnlyService boardReadOnlyService;

    @GetMapping({"", "/"})
    public ModelAndView root(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + "/board/home"));
        return mav;
    }

    @GetMapping("/home")
    public ModelAndView home(
            //
            BoardSearch boardSearch,  //
            HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("board/home");

        BoardSearchType[] searchTypes = BoardSearchType.values();

        mav.addObject("boardSearch", boardSearch);
        mav.addObject("searchTypes", searchTypes);
        return mav;
    }

    @PostMapping("/list")
    public ModelAndView list(
            //
            BoardSearch boardSearch,  //
            HttpServletRequest request, HttpServletResponse response) throws NoContentException {
        ModelAndView mav = new ModelAndView("board/list");

        Page<BoardEntityDto> page = boardReadOnlyService.findAllBy(boardSearch);

        Pageable pageable = page.getPageable();
        List<BoardEntityDto> list = page.getContent();
        long total = page.getTotalElements();

        PagingHelper pagingHelper = PagingHelper.init(pageable, total);

        mav.addObject("boardSearch", boardSearch);
        mav.addObject("pageable", pageable);
        mav.addObject("pagingHelper", pagingHelper);
        mav.addObject("list", list);
        mav.addObject("total", total);

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView register(
            //
            HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("board/register");
        mav.addObject("mode", "insert");
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView view(
            //
            @RequestParam(name = "update", required = false, defaultValue = "false") final boolean updateMode,   //
            @PathVariable("id") final String id,   //
            HttpServletRequest request, HttpServletResponse response) throws NoContentException {
        ModelAndView mav = new ModelAndView("board/register");
        BoardEntityDto boardEntityDto = boardService.findById(id);
        if (updateMode) {
            mav.addObject("mode", "update");
        } else {
            mav.addObject("mode", "view");
        }
        mav.addObject("dto", boardEntityDto);
        return mav;
    }

}
