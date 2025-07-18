package io.github.aglushkovsky.advertisingservice.controller;

import io.github.aglushkovsky.advertisingservice.controller.docs.AuthControllerDocs;
import io.github.aglushkovsky.advertisingservice.dto.request.JwtAuthRequestDto;
import io.github.aglushkovsky.advertisingservice.dto.response.JwtAuthResponseDto;
import io.github.aglushkovsky.advertisingservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtAuthResponseDto login(@RequestBody JwtAuthRequestDto request) {
        log.info("Start POST /api/v1/login");
        JwtAuthResponseDto response = authService.login(request);
        log.info("End POST /api/v1/login; user authenticated");
        return response;
    }
}
