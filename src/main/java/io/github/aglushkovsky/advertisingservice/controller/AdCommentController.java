package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads/{adId}/comments")
@RequiredArgsConstructor
public class AdCommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createCommentForAd(@PathVariable Long adId,
                                                 @RequestBody CommentCreateRequestDto commentCreateRequestDto) {
        return commentService.createComment(adId, commentCreateRequestDto);
    }

    // TODO Сделать параметры pageable по умолчанию
    @GetMapping
    public PageEntity<CommentResponseDto> findAllCommentsByAdId(@PathVariable Long adId,
                                                                @Valid PageableRequestDto pageable) {
        return commentService.findAllCommentsByAdId(adId, pageable);
    }
}
