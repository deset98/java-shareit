package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository("ItemRequestJpaRepository")
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("""
            SELECT ir
            FROM ItemRequest ir
            JOIN FETCH ir.requester r
            WHERE r.id = :userId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> findAllByUserId(long userId);

    @Query("""
            SELECT ir
            FROM ItemRequest ir
            JOIN FETCH ir.requester r
            WHERE r.id != :userId
            ORDER BY ir.created DESC
            """)
    List<ItemRequest> findAllFromOtherUsers(long userId);
}