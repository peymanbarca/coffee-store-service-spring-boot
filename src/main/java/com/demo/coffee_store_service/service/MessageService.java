package com.demo.coffee_store_service.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

//import javax.inject.Inject;
import java.util.Locale;

/**
 * Created by Mahdi on 11/24/2017.
 */
@Service
public class MessageService {


    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired private MessageSource messageSource;



    /**
     * Try to resolve the message. Return default message if no message was found.
     *
     * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
     *             this class are encouraged to base message names on the relevant fully
     *             qualified class name, thus avoiding conflict and ensuring maximum clarity.
     */
    public String getMessage(String code) {
        String langKey = "en";
        langKey = langKey != null ? langKey : "en";
        return messageSource.getMessage(code, null, Locale.forLanguageTag(langKey));
    }

    public String getMessage(String code, String langkey) {
        return messageSource.getMessage(code, null, Locale.forLanguageTag(langkey));
    }
}
