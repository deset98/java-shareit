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
            WHERE o.id = ?1
            """)
    List<Item> getItemsByUserId(long userId);

    @Query("""
            SELECT i
            FROM Item i
            JOIN FETCH i.owner
            WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :substr, '%'))
                  OR LOWER(i.description) LIKE LOWER(CONCAT('%', :substr, '%')))
                  AND i.available = true
            """)
    List<Item> searchItemsBySubstring(String substr);
}