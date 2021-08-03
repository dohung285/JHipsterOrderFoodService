package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Comment;
import com.dohung.orderfood.web.rest.response.CommentObjectResponseDto;
import com.dohung.orderfood.web.rest.response.CommentResponeDto;
import com.dohung.orderfood.web.rest.response.ObjectCountRatingResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    //    Page<Comment> findTop10ByOrderByCreatedDateDesc(Pageable pageable);

    //    @Query(value = "SELECT * FROM Comment c left join Image i on c.food_id = i.food_id ORDER BY c.created_date DESC limit 10 ",nativeQuery = true)
    List<Comment> findTop10ByOrderByCreatedDateDesc();

    @Query(
        "select new com.dohung.orderfood.web.rest.response.CommentObjectResponseDto( c.id,f.name,c.content,c.username,c.rating) from Comment c join Food f on c.foodId = f.id order by c.createdDate desc "
    )
    List<CommentObjectResponseDto> getAll();

    @Query(
        value = "select count( case when x.rating = 5  then 1 else null  end) as '5',count( case when x.rating = 4 then 1 else null end) as '4',count( case when x.rating = 3 then 1 else null end) as '3',count( case when x.rating = 2 then 1 else null end) as '2',count( case when x.rating = 1 then 1 else null end) as '1' from Comment x where x.food_id = :foodId  ",
        nativeQuery = true
    )
    Object countStar(@Param("foodId") Integer foodId);

    List<Comment> getAllByFoodId(Integer foodId);

    @Query(value = "SELECT * FROM Comment c where c.food_id = :foodId ORDER BY c.created_date DESC ", nativeQuery = true)
    List<Comment> findAllByFoodIdAndOrderByCreatedDateDesc(@Param("foodId") Integer foodId);
}
