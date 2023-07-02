package kr.co.demojar.service;

import kr.co.demojar.bean.BoardSearchType;
import kr.co.demojar.bean.dto.BoardEntityDto;
import kr.co.demojar.bean.entity.BoardEntity;
import kr.co.demojar.bean.BoardSearch;
import kr.co.demojar.repository.BoardEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardReadOnlyService {
    private final ModelMapper modelMapper;

    private final BoardEntityRepository boardEntityRepository;


    public Page<BoardEntityDto> findAllBy(final BoardSearch boardSearch) {

        Calendar startDate = stringDateToCal(boardSearch.getSd());
        Calendar endDate = stringDateToCal(boardSearch.getEd());
        BoardSearchType searchType = BoardSearchType.from(boardSearch.getT());


        boardSearch.setPn(boardSearch.getPn() - 1);

        Specification<BoardEntity> search = searchWith(startDate, endDate, searchType, boardSearch.getK());

        Pageable pageable = PageRequest.of(boardSearch.getPn(), boardSearch.getPs(), Sort.by("createdAt").descending());
        Pageable nonePageable = Pageable.unpaged();


        Page<BoardEntity> nonPage = boardEntityRepository.findAll(
                search, nonePageable
        );

        Page<BoardEntity> page = boardEntityRepository.findAll(
                search, pageable
        );

        pageable = page.getPageable();
        List<BoardEntity> list = page.getContent();

        List<BoardEntityDto> dtos = list.stream()    //
                .map(     //
                        (element) -> modelMapper.map(element, BoardEntityDto.class)  //
                )     //
                .collect(Collectors.toList());

        int total = nonPage.getContent().size();

        log.info("total = " + total);

        return new PageImpl<>(dtos, pageable, total);
    }

    private Calendar stringDateToCal(String sd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(sd);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
        }
        return null;
    }


    public static Specification<BoardEntity> searchWith(
            //
            final Calendar startDate,  //
            final Calendar endDate,  //
            final BoardSearchType searchType,  //
            final String searchKeyword  //
    ) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            if (searchType != null && StringUtils.hasText(searchKeyword)) {
                switch (searchType) {
                    case title:
                        predicates.add(builder.like(root.get("title"), "%" + searchKeyword + "%"));
                        break;
                    case body:
                        predicates.add(builder.like(root.get("body"), "%" + searchKeyword + "%"));
                        break;
                    case writer:
                        predicates.add(builder.equal(root.get("writer"), searchKeyword));
                        break;
                }
            }
            Predicate[] arr = predicates.toArray(new Predicate[0]);
            return builder.and(arr);
        });
    }

}
