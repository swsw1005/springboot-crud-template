package kr.co.demojar.service;

import kr.co.demojar.bean.dto.BoardEntityDto;
import kr.co.demojar.bean.entity.BoardEntity;
import kr.co.demojar.exception.NoContentException;
import kr.co.demojar.repository.BoardEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = {Exception.class}, value = Transactional.TxType.REQUIRED)
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    /**
     * <PRE>
     * dummy insert for test
     * </PRE>
     *
     * @return
     */
    public BoardEntityDto testInsert() {
        log.info("start");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS Z");

        BoardEntity boardEntity = BoardEntity.builder()
                .title("test title + " + sdf.format(new Date()))
                .body("test body + " + sdf.format(new Date()))
                .writer(UUID.randomUUID().toString().substring(1, 5))
                .build();

        BoardEntity newEntity = boardEntityRepository.save(boardEntity);

        return modelMapper.map(newEntity, BoardEntityDto.class);
    }

    public BoardEntityDto insert(BoardEntityDto dto) {

        BoardEntity boardEntity = BoardEntity.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .writer(dto.getWriter())
                .build();

        BoardEntity newEntity = boardEntityRepository.save(boardEntity);

        return modelMapper.map(newEntity, BoardEntityDto.class);
    }

    public BoardEntityDto update(BoardEntityDto dto) throws NoContentException {
        String id = dto.getId();

        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById(id);

        if (optionalBoardEntity.isEmpty()) {
            throw new NoContentException("no board found by id [" + id + "]");
        }

        BoardEntity boardEntity = optionalBoardEntity.get();
        boardEntity.update(dto.getTitle(), dto.getBody());

        BoardEntity updateEntity = boardEntityRepository.save(boardEntity);

        return modelMapper.map(updateEntity, BoardEntityDto.class);
    }

    public String deleteById(String id) throws NoContentException {
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById(id);

        if (optionalBoardEntity.isEmpty()) {
            throw new NoContentException("no board found by id [" + id + "]");
        }

        BoardEntity boardEntity = optionalBoardEntity.get();

        boardEntityRepository.deleteById(id);

        return id;
    }

    public int deleteByIds(final String ids) throws NoContentException {

        Set<String> idSet = new HashSet<>();

        if (ids == null) {
            throw new NoContentException("ids is null");
        } else if (ids.trim().isEmpty()) {
            throw new NoContentException("ids is empty");
        }

        String[] arr = ids.split(",");
        for (String s : arr) {
            idSet.add(s.trim());
        }
        List<BoardEntity> existEntities = boardEntityRepository.findAllById(idSet);
        final int cnt = existEntities.size();
        boardEntityRepository.deleteInBatch(existEntities);
        return cnt;
    }

    public BoardEntityDto findById(String id) throws NoContentException {
        Optional<BoardEntity> optionalBoardEntity = boardEntityRepository.findById(id);

        if (optionalBoardEntity.isEmpty()) {
            throw new NoContentException("board not exist in id [" + id + "]");
        }

        BoardEntity boardEntity = optionalBoardEntity.get();

        return modelMapper.map(boardEntity, BoardEntityDto.class);
    }
}
