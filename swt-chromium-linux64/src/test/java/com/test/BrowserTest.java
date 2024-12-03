package com.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.swt.chromium.Browser;
import com.tulin.v8.swt.chromium.LoadEvent;
import com.tulin.v8.swt.chromium.LoadListenerAdapter;

public class BrowserTest {
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.MIN | SWT.MAX | SWT.CLOSE | SWT.RESIZE);
		shell.setText("Chromium Test");
		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE, "https://tlv8.com");
		browser.addLoadListener(new LoadListenerAdapter() {
			@Override
			public void onLoadEnd(LoadEvent event) {
				System.out.println("load end:" + event.httpStatusCode);
				super.onLoadEnd(event);
			}
		});
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		System.exit(0);
	}

}
