package io.github.aglushkovsky.advertisingservice.controller.docs;

import io.github.aglushkovsky.advertisingservice.dao.PageEntity;
import io.github.aglushkovsky.advertisingservice.dto.request.CommentCreateRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.request.PageableRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;

@Tag(name = "Comment", description = "Create-read operations for comments")
public interface AdCommentControllerDocs {

    @Operation(
            summary = "Find all comments by ad id",
            parameters = @Parameter(
                    name = "adId",
                    description = "Target Ad ID"
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns page with ad comments on specified page number",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "body": [
                                                        {
                                                          "id": 1,
                                                          "author": {
                                                            "id": 1,
                                                            "login": "test_user",
                                                            "email": null,
                                                            "phoneNumber": null,
                                                            "totalRating": 0
                                                          },
                                                          "createdAt": "2025-05-19T19:11:26.165173",
                                                          "text": "Слишком дорого"
                                                        }
                                                      ],
                                                      "metadata": {
                                                        "currentPage": 1,
                                                        "totalPages": 1,
                                                        "totalRecords": 1,
                                                        "isLastPage": true
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Returns when found constraint violation",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "about:blank",
                                              "title": "Bad Request",
                                              "status": 400,
                                              "instance": "/api/v1/ads/1",
                                              "errors": [
                                                {
                                                  "parameter": "text",
                                                  "message": "Текст комментария не может быть меньше 5 символов"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    @SecurityRequirements
    CommentResponseDto createCommentForAd(@Min(1) Long adId, @Valid CommentCreateRequestDto commentCreateRequestDto);

    @Operation(
            summary = "Find all comments by ad id",
            description = "Returns all ad comments on specified page with page metadata",
            parameters = @Parameter(
                    name = "adId",
                    description = "Target Ad ID"
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Page with ad comments on specified page",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "body": [
                                                        {
                                                          "id": 1,
                                                          "author": {
                                                            "id": 1,
                                                            "login": "test_user",
                                                            "email": null,
                                                            "phoneNumber": null,
                                                            "totalRating": 0
                                                          },
                                                          "createdAt": "2025-05-19T19:11:26.165173",
                                                          "text": "Слишком дорого"
                                                        }
                                                      ],
                                                      "metadata": {
                                                        "currentPage": 1,
                                                        "totalPages": 1,
                                                        "totalRecords": 1,
                                                        "isLastPage": true
                                                      }
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    @SecurityRequirements
    PageEntity<CommentResponseDto> findAllCommentsByAdId(@Min(1) Long adId,
                                                         @ParameterObject @Valid PageableRequestDto pageable);
}
