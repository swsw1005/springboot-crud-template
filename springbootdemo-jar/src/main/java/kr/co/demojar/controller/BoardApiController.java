package kr.co.demojar.controller;

import com.google.gson.Gson;
import kr.co.demojar.bean.dto.BoardEntityDto;
import kr.co.demojar.exception.NoContentException;
import kr.co.demojar.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    private final Gson gson;

    /**
     * <PRE>
     * PUT /api/v1/board
     * insert new board
     * </PRE>
     *
     * @param dto
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/board", method = {RequestMethod.PUT})
    public ResponseEntity<BoardEntityDto> insert(
            //
            @Validated BoardEntityDto dto, //
            HttpServletRequest request, HttpServletResponse response) //
    {
        BoardEntityDto insertedDto = boardService.insert(dto);
        return new ResponseEntity<>(insertedDto, HttpStatus.OK);
    }

    /**
     * <PRE>
     * PATCH /api/v1/board
     * update existing board
     * </PRE>
     *
     * @param dto
     * @param request
     * @param response
     * @return
     * @throws NoContentException
     */
    @RequestMapping(value = "/board", method = {RequestMethod.POST})
    public ResponseEntity<BoardEntityDto> update(
            //
            @Validated BoardEntityDto dto, //
            HttpServletRequest request, HttpServletResponse response) //
            throws NoContentException {

        BoardEntityDto insertedDto = boardService.update(dto);
        return new ResponseEntity<>(insertedDto, HttpStatus.OK);
    }

    /**
     * <PRE>
     * DELETE /api/v1/board/{id}
     * delete  existing board
     * </PRE>
     *
     * @param id
     * @param request
     * @param response
     * @return
     * @throws NoContentException
     */
    @RequestMapping(value = "/board/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<String> delete(
            //
            @PathVariable(value = "id") String id, //
            HttpServletRequest request, HttpServletResponse response) throws NoContentException //
    {
        String deletedId = boardService.deleteById(id);
        return new ResponseEntity<>(deletedId, HttpStatus.OK);
    }

    /**
     * <PRE>
     * POST /api/v1/deleteBulk
     * delete all by ids
     * </PRE>
     *
     * @param ids      ex) 1423,1253,12,4353 ....
     * @param request
     * @param response
     * @return
     * @throws NoContentException
     */
    @RequestMapping(value = "/board/deleteBulk", method = {RequestMethod.POST})
    public ResponseEntity<Integer> deleteBulk(
            //
            @RequestParam(name = "ids") String ids,  //
            HttpServletRequest request, HttpServletResponse response) throws NoContentException //
    {
        int deleteCnt = boardService.deleteByIds(ids);
        return new ResponseEntity<>(deleteCnt, HttpStatus.OK);
    }

}
