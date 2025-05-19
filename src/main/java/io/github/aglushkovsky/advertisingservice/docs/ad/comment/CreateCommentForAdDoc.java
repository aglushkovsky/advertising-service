package io.github.aglushkovsky.advertisingservice.docs.ad.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Create comment for ad",
        parameters = @Parameter(
                name = "adId",
                description = "Returns when ad ID to add a comment on"
        ),
        requestBody = @RequestBody(
                description = "Request to create comment",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        ),
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = """
                                Returns when comment added successfully.
                                Response contains created comment representation
                                """,
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                          "id": 1,
                                                          "author": {
                                                            "id": 1,
                                                            "login": "test_user",
                                                            "email": "example@example.com",
                                                            "phoneNumber": "+79201234567",
                                                            "totalRating": 4.7
                                                          },
                                                          "createdAt": "2025-05-19T12:18:32.910Z",
                                                          "text": "Не бита, не крашена? С пробегом по РФ?"
                                                        }
                                                        """
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Returns when ad with specified id does not exists",
                        content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProblemDetail.class),
                                examples = {
                                        @ExampleObject(
                                                value = """
                                                        {
                                                            "type": "about:blank",
                                                            "title": "Resource Not Found",
                                                            "status": 404,
                                                            "detail": "Элемент с id=321312 не найден",
                                                            "instance": "/api/v1/ads/321312/comments"
                                                        }
                                                        """
                                        )
                                }
                        )
                )
        }
)
public @interface CreateCommentForAdDoc {
}
