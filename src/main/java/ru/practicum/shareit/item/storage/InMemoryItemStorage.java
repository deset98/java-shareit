package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

@Repository("InMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage {
    private final HashMap<Long, Item> items = new HashMap<>();
    private long itemId;


    @Override
    public Item addOne(Item item) {
        item.setId(++itemId);
        items.put(itemId, item);
        return items.get(itemId);
    }

    @Override
    public Item updateOne(long userId, long itemId, ItemDto itemDto) {
        Item item = items.get(itemId);

        if (item != null && item.getOwnerId() == userId) {
            if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
                item.setDescription(itemDto.getDescription());
            }
            item.setAvailable(itemDto.getAvailable());
        } else {
            if (item == null) {
                throw new NotFoundException("Item id {} не найден в памяти приложения", itemId);
            }
            if (item.getOwnerId() != userId) {
                throw new NotFoundException("User с id {}, не является владельцем Item с id {}", userId, itemId);
            }
        }
        return item;
    }

    @Override
    public Item getOne(long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Item id {} не найден в памяти приложения", itemId);
        }
        return item;
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public List<Item> searchItemsBySubstring(String substring) {
        String lowerCaseSubstring = substring.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseSubstring) ||
                        item.getDescription().toLowerCase().contains(lowerCaseSubstring)) &&
                        item.getAvailable().equals(true))
                .toList();
    }
}