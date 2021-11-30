package com.titoschmidt.codelab.fitnesstracker.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Conversor {

    public static double round(double valor, int casas) {
        if (casas < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(casas, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
