package com.hackuniversity.parser;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;

public class ParserTest {

    Parser parser = new Parser();
    String s = "25.05.2019-18:30 - \"Через 5 мин по моему сигналу поздравим барабанщика с Днем Рождения!\"";
    String numbers = "+79228142319,+79995354001,+79992268443,+79062547839";

    @Test
    public void parseDate() throws ParseException {
        System.out.println(parser.parseDate(s));
    }

    @Test
    public void parseMessage() {
        String result = parser.parseMessage(s);
        Assert.assertEquals(result, "Через 5 мин по моему сигналу поздравим барабанщика с Днем Рождения!");
        System.out.println(result);
    }

    @Test
    public void parseCronExpr() throws ParseException {
        Assert.assertEquals(parser.parseCronExpr(parser.parseDate(s)), "0 30 18 25 5 ?");
    }

    @Test
    public void parseNumbers() {
        Assert.assertEquals(Arrays.asList("+79228142319", "+79995354001", "+79992268443", "+79062547839"),
                parser.parseNumbers(numbers));
    }
}