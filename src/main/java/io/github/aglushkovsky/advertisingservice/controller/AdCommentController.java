package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.docs.ad.comment.CreateCommentForAdDoc;
import io.github.aglushkovsky.advertisingservice.docs.ad.comment.FindAllCommentsByAdIdDoc;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads/{adId}/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comment", description = "Create-read operations for comments")
public class AdCommentController {

    private final CommentService commentService;

    @PostMapping
    @CreateCommentForAdDoc
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createCommentForAd(@PathVariable @Min(1) Long adId,
                                                 @RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto) {
        log.info("Start POST /api/v1/ads/{}/comments", adId);
        CommentResponseDto response = commentService.createComment(adId, commentCreateRequestDto);
        log.info("End POST /api/v1/ads/{}/comments", adId);
        return response;
    }

    @GetMapping
    @FindAllCommentsByAdIdDoc
    public PageEntity<CommentResponseDto> findAllCommentsByAdId(@PathVariable @Min(1) Long adId,
                                                                @ParameterObject @Valid PageableRequestDto pageable) {
        log.info("Start GET /api/v1/ads/{}/comments", adId);
        PageEntity<CommentResponseDto> response = commentService.findAllCommentsByAdId(adId, pageable);
        log.info("End GET /api/v1/ads/{}/comments", adId);
        return response;
    }
}
