package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository("ItemJpaRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("""
            SELECT i
            FROM Item i
            JOIN FETCH i.owner o
            WHERE o.id = :userId
            """)
    List<Item> findAllByUserId(long userId);

    @Query("""
            SELECT i
            FROM Item i
            JOIN FETCH i.owner
            WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :substr, '%'))
                  OR LOWER(i.description) LIKE LOWER(CONCAT('%', :substr, '%')))
                  AND i.available = true
            """)
    List<Item> findAllBySubstring(String substr);

    @Query("""
            SELECT i
            FROM Item i
            JOIN FETCH i.request r
            WHERE r.id = :requestId
            """)
    List<Item> findAllRepliesByRequestId(long requestId);
}