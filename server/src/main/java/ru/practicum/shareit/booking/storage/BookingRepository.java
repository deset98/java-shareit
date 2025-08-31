package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository("BookingJpaRepository")
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId);

    List<Booking> findAllByItemOwnerId(long ownerId);

    @Query("""
            SELECT b.end
            FROM Booking b
            WHERE b.item.id = ?1 AND b.item.owner.id = ?2 AND b.start < CURRENT_TIMESTAMP AND b.status = 'APPROVED'
            ORDER BY b.start DESC
            LIMIT 1
            """)
    Optional<Instant> findLastBooking(long itemId, long userId);

    @Query("""
            SELECT b.start
            FROM Booking b
            WHERE b.item.id = ?1 AND b.item.owner.id = ?2 AND b.start > CURRENT_TIMESTAMP AND b.status = 'APPROVED'
            ORDER BY b.start ASC
            LIMIT 1
            """)
    Optional<Instant> findNextBooking(long itemId, long userId);

    boolean existsByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, Instant end);
}