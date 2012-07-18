/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.context.ResponseStream;

/**
 *
 * @author Christian Beikov
 */
public class BlazeResponseStream extends ResponseStream{

    public static final int CR = '\r';
    public static final int LF = '\n';
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    public String getContent(){
        return new String(baos.toByteArray());
    }

    @Override
    public void write(int b) throws IOException {
        if(b != CR && b != LF)
            baos.write(b);
    }

}
