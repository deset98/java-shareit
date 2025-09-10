package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.Headers;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(@Qualifier("BookingServiceImpl") final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(Headers.USER_ID) long userId,
                                            @RequestBody final BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(Headers.USER_ID) final long userId,
                                             @PathVariable("bookingId") final long bookingId,
                                             @RequestParam("approved") final boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(Headers.USER_ID) final long userId,
                                         @PathVariable("bookingId") final long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getListByUser(@RequestHeader(Headers.USER_ID) final long userId,
                                                  @RequestParam(name = "state",
                                                          required = false,
                                                          defaultValue = "ALL") final State state) {
        return bookingService.getListByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getListByOwner(@RequestHeader(Headers.USER_ID) final long ownerId,
                                                   @RequestParam(name = "state",
                                                           required = false,
                                                           defaultValue = "ALL") final State state) {
        return bookingService.getListByOwner(ownerId, state);
    }
}
