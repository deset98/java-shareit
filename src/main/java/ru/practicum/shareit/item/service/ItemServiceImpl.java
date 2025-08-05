package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;

@Service("ItemServiceImpl")
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(@Qualifier("InMemoryItemStorage") final ItemStorage itemStorage,
                           @Qualifier("InMemoryUserStorage") final UserStorage userStorage
    ) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        if (userStorage.getOne(userId) == null) {
            throw new NotFoundException("Нельзя создать Item. User id = {} не существует", userId);
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemStorage.addOne(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemStorage.updateOne(userId, itemId, itemDto));
    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.toItemDto(itemStorage.getOne(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemStorage.getItemsByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItemsBySubstring(String substring) {
        if (substring.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemStorage.searchItemsBySubstring(substring).stream()
                    .map(ItemMapper::toItemDto)
                    .toList();
        }
    }
}