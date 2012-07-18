/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Christian Beikov
 */
public class BlazeResponseWriter extends StringWriter{

    public static final int CR = '\r';
    public static final int LF = '\n';

    public String getContent(){
        return getBuffer().toString();
    }

    @Override
    public void write(int c) {
        if(c != CR && c != LF)
            super.write(c);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }
    
    @Override
    public void write(char[] cbuf, int off, int len) {
        write(new String(cbuf), off, len);
    }

    @Override
    public void write(String str) {
        super.write(str.replaceAll("\n|\r\n", ""));
    }

    @Override
    public void write(String str, int off, int len) {
        write(str.substring(off, off + len));
    }
    
}
