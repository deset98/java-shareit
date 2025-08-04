package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item addOne(Item item);

    Item updateOne(long userId, long itemId, ItemDto itemDto);

    Item getOne(long itemId);

    List<Item> getItemsByUserId(long userId);

    List<Item> searchItemsBySubstring(String substring);
}