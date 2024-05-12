package com.tulin.v8.swt.chromium;

import org.eclipse.swt.internal.SWTEventListener;

public interface LoadListener extends SWTEventListener{
	public void onLoadingStateChange(LoadEvent event);
	public void onLoadStart(LoadEvent event);
	public void onLoadEnd(LoadEvent event);
	public void onLoadError(LoadEvent event);
}
