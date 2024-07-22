package ru.aphecoculture.tgbot.gitlab.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
 
@Component
public class ReportCache extends ConcurrentHashMap<Integer, String> {
}
