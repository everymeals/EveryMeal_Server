package everymeal.server.global.exception.controller;

import static everymeal.server.global.exception.ExceptionList.PATH_NOT_FOUND;

import everymeal.server.global.exception.ApplicationException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController implements ErrorController {

    @GetMapping("/error")
    public String error() {
        throw new ApplicationException(PATH_NOT_FOUND);
    }
}
