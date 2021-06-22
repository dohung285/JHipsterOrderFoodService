package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAll(Pageable pageable);

    List<Comment> findAllByFoodId(Integer foodId);
}
