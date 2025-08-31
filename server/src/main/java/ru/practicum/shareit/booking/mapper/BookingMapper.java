package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.ZoneId;

public class BookingMapper {

    public static Booking toBooking(final BookingRequestDto bookingRequestDto) {
        return Booking.builder()
                .start(bookingRequestDto.getStart().atZone(ZoneId.systemDefault()).toInstant())
                .end(bookingRequestDto.getEnd().atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }

    public static BookingResponseDto toBookingResponseDto(final Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .end(booking.getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }
}