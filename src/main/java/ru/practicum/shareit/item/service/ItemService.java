package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDtoExtended getItem(long itemId);

    List<ItemDtoExtended> getItemsByUser(long userId);

    List<ItemDto> searchItemsBySubstring(String substring);

    CommentResponseDto addComment(long userId, long itemId, CommentRequestDto commentRequestDto);
}