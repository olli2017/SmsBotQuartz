package com.hackuniversity.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Parser {

    public Date parseDate(String s) throws ParseException {
        SimpleDateFormat obj = new SimpleDateFormat("dd.MM.yyyy-hh:mm");

        return obj.parse(s);
    }

    public String parseMessage(String s) {
        return s.substring(s.indexOf("\"") + 1, s.length() - 1);
    }

    public String parseCronExpr(Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append("0")
                .append(" ")
                .append(date.getMinutes())
                .append(" ")
                .append(date.getHours())
                .append(" ")
                .append(date.getDate())
                .append(" ")
                .append(date.getMonth() + 1)
                .append(" ")
                .append("?");
        return sb.toString();
    }

    public List<String> parseNumbers(String numbers) {
        return Arrays.asList(numbers.split(","));
    }

}
