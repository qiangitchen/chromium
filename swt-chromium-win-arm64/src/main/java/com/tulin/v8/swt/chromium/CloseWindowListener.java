package com.tulin.v8.swt.chromium;

import org.eclipse.swt.internal.SWTEventListener;

public interface CloseWindowListener extends SWTEventListener{
	public void close(WindowEvent event);
}
