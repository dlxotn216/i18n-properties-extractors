package message.properties.locale.application.service.impl;

import message.properties.locale.application.service.LocaleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by taesu on 2018-08-16.
 */
@Service
public class LocaleServiceImpl implements LocaleService{

    @Override
    public List<Locale> getLocales(){
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> !StringUtils.isEmpty(locale.getLanguage()))
                .collect(Collectors.toList());
    }
}
