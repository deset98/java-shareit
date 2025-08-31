package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {

        log.debug("POST/items - adding new item {} by user {}",
                itemDto.getName(),
                userId);

        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@Valid @RequestBody ItemDto itemDto,
                                           @PathVariable long itemId,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {

        log.debug("PATCH/item/id - adding new item {} by user {}",
                itemDto.getName(),
                userId);

        return itemClient.editItem(itemDto, itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.debug("GET/items: all items of the user {} returned", userId);

        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId) {

        log.debug("GET/items/id: returning item {}", itemId);

        return itemClient.getItem(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {

        log.debug("GET/items: all items of the containing text {}", text);

        if (text == null || text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @Valid @RequestBody CommentRequestDto commentDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.addComment(itemId, commentDto, userId);
    }
}