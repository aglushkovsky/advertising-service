package io.github.aglushkovsky.advertisingservice.docs.ad.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
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
public @interface FindAllCommentsByAdIdDoc {
}
