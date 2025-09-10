package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.RespItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.Instant;
import java.time.ZoneId;

public class ItemRequestMapper {

    public static RespItemRequestDto toRespItemRequestDto(ItemRequest itemRequest) {
        return RespItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .requesterId(itemRequest.getRequester().getId())
                .build();
    }

    public static ItemRequest toItemRequest(NewItemRequestDto dto) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .created(Instant.now())
                .build();
    }
}
