package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.AdCommentControllerDocs;
import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.github.aglushkovsky.advertisingservice.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ads/{adId}/comments")
@RequiredArgsConstructor
@Slf4j
public class AdCommentController implements AdCommentControllerDocs {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createCommentForAd(@PathVariable @Min(1) Long adId,
                                                 @RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto) {
        log.info("Start POST /api/v1/ads/{}/comments", adId);
        CommentResponseDto response = commentService.createComment(adId, commentCreateRequestDto);
        log.info("End POST /api/v1/ads/{}/comments", adId);
        return response;
    }

    @GetMapping
    public PageEntity<CommentResponseDto> findAllCommentsByAdId(@PathVariable @Min(1) Long adId,
                                                                @Valid PageableRequestDto pageable) {
        log.info("Start GET /api/v1/ads/{}/comments", adId);
        PageEntity<CommentResponseDto> response = commentService.findAllCommentsByAdId(adId, pageable);
        log.info("End GET /api/v1/ads/{}/comments", adId);
        return response;
    }
}
