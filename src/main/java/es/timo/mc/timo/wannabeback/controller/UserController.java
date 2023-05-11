package es.timo.mc.timo.wannabeback.controller;

import es.timo.mc.timo.wannabeback.controller.helper.UserHelperController;
import es.timo.mc.timo.wannabeback.model.dto.request.LoginRequest;
import es.timo.mc.timo.wannabeback.model.dto.response.LoginResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "User")
@RequiredArgsConstructor
public class UserController extends BaseErrorController {

    /**
     * The User helper controller.
     */
    private final UserHelperController userHelperController;


    /**
     * Refres token.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    @GetMapping("/refresh-token")
    private void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.userHelperController.refreshToken(request, response);
    }

    /**
     * Login response entity.
     *
     * @param request      the request
     * @param loginRequest the login request
     * @return the response entity
     */
    @PostMapping("/login")
    private ResponseEntity<LoginResponse> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userHelperController.login(request, loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

}
