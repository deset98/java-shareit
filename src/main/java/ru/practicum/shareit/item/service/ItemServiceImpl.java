package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service("ItemServiceImpl")
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(@Qualifier("ItemJpaRepository") final ItemRepository itemRepository,
                           @Qualifier("UserJpaRepository") final UserRepository userRepository,
                           @Qualifier("BookingJpaRepository") final BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нельзя создать Item. User id = {} не существует", userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Нельзя обновить Item. User id = {} не существует", userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Нельзя обновить Item. Item id = {} не существует", itemId));
        ItemMapper.updateItem(item, itemDto);
        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDtoExtended getItem(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item id = {} не существует", itemId));
        return ItemMapper.toItemDtoExtended(item);
    }

    @Override
    public List<ItemDtoExtended> getItemsByUser(long userId) {

        return itemRepository.getItemsByUserId(userId).stream()
                .map(ItemMapper::toItemDtoExtended)
                .peek(dto -> this.setLastAndNextBooking(dto, userId))
                .toList();
    }

    @Override
    public List<ItemDto> searchItemsBySubstring(String substr) {
        if (substr.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemRepository.searchItemsBySubstring(substr).stream()
                    .map(ItemMapper::toItemDto)
                    .toList();
        }
    }

    @Override
    @Transactional
    public CommentResponseDto addComment(long userId, long itemId, CommentRequestDto commentRequestDto) {

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, Instant.now())) {
            throw new BadRequestException("User id={} не арендовал Item id={}", userId, itemId);
        }

        Comment comment = CommentMapper.toComment(commentRequestDto);
        comment.setItem(itemRepository.findById(itemId).get());
        comment.setAuthor(userRepository.findById(userId).get());
        comment.setCreated(Instant.now());

        comment = commentRepository.save(comment);

        return CommentMapper.toCommentResponseDto(comment);
    }

    private void setLastAndNextBooking(ItemDtoExtended dto, long userId) {
        bookingRepository.findLastBooking(dto.getId(), userId)
                .ifPresent(instant -> {
                    LocalDateTime last = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    dto.setLastBooking(last);
                });
        bookingRepository.findNextBooking(dto.getId(), userId)
                .ifPresent(instant -> {
                    LocalDateTime next = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    dto.setLastBooking(next);
                });
    }
}