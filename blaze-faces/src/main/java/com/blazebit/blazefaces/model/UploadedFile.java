/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.model;

import java.io.IOException;
import java.io.InputStream;

public interface UploadedFile {

    public String getFileName();

    public InputStream getInputStream() throws IOException;

    public long getSize();

    public byte[] getContents();

    public String getContentType();
}
