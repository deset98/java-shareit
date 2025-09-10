package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service("BookingServiceImpl")
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(@Qualifier("BookingJpaRepository") final BookingRepository bookingRepository,
                              @Qualifier("UserJpaRepository") final UserRepository userRepository,
                              @Qualifier("ItemJpaRepository") final ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingResponseDto create(long userId, BookingRequestDto bookingRequestDto) {

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id={} не существует", userId));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item id={} не существует", bookingRequestDto.getItemId()));

        if (!item.getAvailable()) {
            throw new BadRequestException("Item id={}, не доступен для бронирования", item.getId());
        }

        if (userId != item.getOwner().getId()) {

            Booking booking = BookingMapper.toBooking(bookingRequestDto);
            booking.setItem(item);
            booking.setBooker(booker);
            booking.setStatus(Status.WAITING);

            booking = bookingRepository.save(booking);
            return BookingMapper.toBookingResponseDto(booking);
        } else {
            throw new ForbiddenException("User id={} не явл. владельцем Item id={}", userId, item.getId());
        }
    }

    @Override
    @Transactional
    public BookingResponseDto approve(long userId,
                                      long bookingId,
                                      boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование id={} не найдено", bookingId));

        long ownerId = booking.getItem().getOwner().getId();
        if (userId != ownerId) {
            throw new ForbiddenException("User id={} не явл. владельцем Item id={}", userId, booking.getItem().getId());
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto get(long userId, long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование id={} не найдено", bookingId));


        boolean userIsOwnerOfItem = booking.getItem().getOwner().getId() == userId;
        boolean userIsAuthorOfBooking = booking.getBooker().getId() == userId;

        if (userIsOwnerOfItem || userIsAuthorOfBooking) {
            return BookingMapper.toBookingResponseDto(booking);
        } else {
            throw new ForbiddenException("User id={} не явл. владельцем или автором Booking id={}", userId, bookingId);
        }
    }

    @Override
    public List<BookingResponseDto> getListByUser(long userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id={} не существует", userId);
        }

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerId(userId)
                        .stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .toList();
            default:
                throw new ForbiddenException("Неверно указан параметр state={}", state);
        }
    }

    @Override
    public List<BookingResponseDto> getListByOwner(long ownerId, State state) {
        if (!bookingRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь с id={} не существует", ownerId);
        }

        bookingRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id={} не существует", ownerId));
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerId(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingResponseDto)
                        .toList();
            default:
                throw new ForbiddenException("Неверно указан параметр state={}", state);
        }
    }
}
