package com.david.hlp.SpringBootWork.blog.Repository;

import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleTable, Long> {
}
