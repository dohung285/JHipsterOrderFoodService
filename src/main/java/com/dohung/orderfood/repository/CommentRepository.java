package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Comment;
import com.dohung.orderfood.web.rest.response.CommentObjectResponseDto;
import com.dohung.orderfood.web.rest.response.CommentResponeDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    //    Page<Comment> findTop10ByOrderByCreatedDateDesc(Pageable pageable);

    //    @Query(value = "SELECT * FROM Comment c left join Image i on c.food_id = i.food_id ORDER BY c.created_date DESC limit 10 ",nativeQuery = true)
    List<Comment> findTop10ByOrderByCreatedDateDesc();

    List<Comment> findAllByFoodId(Integer foodId);

    @Query(
        "select new com.dohung.orderfood.web.rest.response.CommentObjectResponseDto( c.id,f.name,c.content,c.username,c.rating) from Comment c join Food f on c.foodId = f.id "
    )
    List<CommentObjectResponseDto> getAll();
}
