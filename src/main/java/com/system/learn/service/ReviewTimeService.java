package com.system.learn.service;

import com.system.learn.dto.card.ReviewTimeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class ReviewTimeService {
    @Autowired
    private MessageSource messageSource;

    public ReviewTimeDto getTimeUntilNextReview(LocalDateTime nextReviewAt, Locale locale) {
        if (nextReviewAt == null) {
            return new ReviewTimeDto(0.0, messageSource.getMessage("time.can_review", null, locale));
        }

        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), nextReviewAt);

        if (minutes <= 0) {
            return new ReviewTimeDto(0.0, messageSource.getMessage("time.can_review", null, locale));
        }

        if (minutes < 60) {
            String unit = getPluralForm(minutes, "time.minute", "time.minutes", "time.minut", locale);
            return new ReviewTimeDto(minutes, unit);
        }

        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), nextReviewAt);
        if (hours < 24) {
            String unit = getPluralForm(hours, "time.hour", "time.hours", "time.hourss", locale);
            return new ReviewTimeDto(hours, unit);
        }

        long days = ChronoUnit.DAYS.between(LocalDateTime.now(), nextReviewAt);
        if (days < 30) {
            String unit = getPluralForm(days, "time.day", "time.days", "time.dayss", locale);
            return new ReviewTimeDto(days, unit);
        }

        long months = ChronoUnit.MONTHS.between(LocalDateTime.now(), nextReviewAt);
        if (months < 12) {
            String unit = getPluralForm(months, "time.month", "time.months", "time.monthss", locale);
            return new ReviewTimeDto(months, unit);
        }

        double years = days / 365.25;
        double roundedYears = Math.round(years * 10) / 10.0;

        // Для годов с десятичной частью
        if (roundedYears % 1 != 0) {
            return new ReviewTimeDto(roundedYears,
                    messageSource.getMessage("time.decimal_years", null, locale));
        }

        // Для целых лет
        long wholeYears = (long) roundedYears;
        String unit = getPluralForm(wholeYears, "time.year", "time.years", "time.yearss", locale);
        return new ReviewTimeDto(roundedYears, unit);
    }

    private String getPluralForm(long number, String form1Key, String form2Key, String form5Key, Locale locale) {
        number = Math.abs(number);
        number %= 100;
        if (number >= 11 && number <= 19) {
            return messageSource.getMessage(form5Key, null, locale);
        }
        number %= 10;
        if (number == 1) {
            return messageSource.getMessage(form1Key, null, locale);
        }
        if (number >= 2 && number <= 4) {
            return messageSource.getMessage(form2Key, null, locale);
        }
        return messageSource.getMessage(form5Key, null, locale);
    }
}