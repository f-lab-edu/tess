package com.flab.tess.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final NumberFormat krwFormat = NumberFormat.getNumberInstance(Locale.KOREA);

    public static String formatKRW(BigDecimal value) {
        if (value == null) {
            return "₩0 원";
        }
        return krwFormat.format(value);
    }

}
