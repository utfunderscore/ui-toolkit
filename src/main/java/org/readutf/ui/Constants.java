package org.readutf.ui;

import org.intellij.lang.annotations.Subst;

import java.util.concurrent.atomic.AtomicInteger;

public class Constants {

    @Subst("")
    public static AtomicInteger fileNameCounter = new AtomicInteger(0);
}
