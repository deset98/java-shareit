package ru.practicum.shareit.request.ItemRequestService;

import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.RespItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    RespItemRequestDto create(long userId, NewItemRequestDto dto);

    List<RespItemRequestDto> getAllByUserId(long userId);

    List<RespItemRequestDto> getAllFromOtherUsers(long userId);

    RespItemRequestDto getByRequestId(long requestId);
}