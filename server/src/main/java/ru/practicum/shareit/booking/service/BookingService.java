package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approve(long userId, long bookingId, boolean approved);

    BookingResponseDto get(long userId, long bookingId);

    List<BookingResponseDto> getListByUser(long userId, State state);

    List<BookingResponseDto> getListByOwner(long ownerId, State state);

}