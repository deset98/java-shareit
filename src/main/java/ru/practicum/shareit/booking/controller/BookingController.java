package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

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
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody final BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") final long userId,
                                             @PathVariable("bookingId") final long bookingId,
                                             @RequestParam("approved") final boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") final long userId,
                                         @PathVariable("bookingId") final long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getListByUser(@RequestHeader("X-Sharer-User-Id") final long userId,
                                                  @RequestParam(name = "state",
                                                          required = false,
                                                          defaultValue = "ALL") final State state) {
        return bookingService.getListByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getListByOwner(@RequestHeader("X-Sharer-User-Id") final long ownerId,
                                                   @RequestParam(name = "state",
                                                           required = false,
                                                           defaultValue = "ALL") final State state) {
        return bookingService.getListByOwner(ownerId, state);
    }
}
