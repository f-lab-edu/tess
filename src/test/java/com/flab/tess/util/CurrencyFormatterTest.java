package com.flab.tess.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatterTest {

    private static final NumberFormat krwFormat = NumberFormat.getNumberInstance(Locale.KOREA);

    @Test
    public void testFormatKRW_NULL_일때_0원_반환() {
        assertEquals("₩0 원", CurrencyFormatter.formatKRW(null));
    }

    @Test
    public void testFormatKRW_0_일때_0원_반환() {
        BigDecimal value = BigDecimal.ZERO;
        assertEquals(krwFormat.format(value), CurrencyFormatter.formatKRW(value));
    }

    @Test
    public void testFormatKRW_원화_변환_확인() {
        BigDecimal value = new BigDecimal("550000");
        assertEquals(krwFormat.format(value), CurrencyFormatter.formatKRW(value));
    }


}
