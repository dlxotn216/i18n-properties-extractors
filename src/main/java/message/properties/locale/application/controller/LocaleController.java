package message.properties.locale.application.controller;

import message.properties.locale.application.service.LocaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * Created by taesu on 2018-08-16.
 */
@RestController
public class LocaleController {

    private LocaleService localeService;

    public LocaleController(LocaleService localeService) {
        this.localeService = localeService;
    }

    @GetMapping("locales")
    public List<Locale> getLocales() {
        return localeService.getLocales();
    }
}
