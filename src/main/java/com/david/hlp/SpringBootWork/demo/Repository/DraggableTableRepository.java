package com.david.hlp.SpringBootWork.demo.Repository;

import com.david.hlp.SpringBootWork.demo.entity.DraggableTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraggableTableRepository extends JpaRepository<DraggableTable, Long> {
}
