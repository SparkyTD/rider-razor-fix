package com.sparky.rider;

import java.lang.instrument.Instrumentation;

public class RiderAgent {
    public static void premain(String args, Instrumentation instrumentation){
        // instrumentation.addTransformer(new RazorClassFileTransformer());
    }
}
