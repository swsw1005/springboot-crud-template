package kr.co.demojar.repository;

import kr.co.demojar.bean.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardEntityRepository extends JpaRepository<BoardEntity, String>, JpaSpecificationExecutor<BoardEntity> {
    Page<BoardEntity> findAll(Specification<BoardEntity> spec, Pageable pageable);

}