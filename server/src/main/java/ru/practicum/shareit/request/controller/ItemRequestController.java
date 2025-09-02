package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestService.ItemRequestService;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.RespItemRequestDto;
import ru.practicum.shareit.util.Headers;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    public ItemRequestController(@Qualifier("ItemRequestServiceImpl") final ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public RespItemRequestDto create(@RequestHeader(Headers.USER_ID) long userId,
                                     @RequestBody final NewItemRequestDto dto) {
        return itemRequestService.create(userId, dto);
    }

    @GetMapping
    public List<RespItemRequestDto> getAllByUserId(@RequestHeader(Headers.USER_ID) long userId) {
        return itemRequestService.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<RespItemRequestDto> getAllFromOtherUsers(@RequestHeader(Headers.USER_ID) long userId) {
        return itemRequestService.getAllFromOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public RespItemRequestDto getByRequestId(@PathVariable("requestId") final long requestId) {
        return itemRequestService.getByRequestId(requestId);
    }
}