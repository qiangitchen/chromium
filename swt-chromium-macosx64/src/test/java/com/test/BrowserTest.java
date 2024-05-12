package com.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tulin.v8.swt.chromium.Browser;

public class BrowserTest {
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.MIN | SWT.MAX | SWT.CLOSE | SWT.RESIZE);

		shell.setLocation(new Point(800, 600));
		shell.setText("Chromium Test");
		shell.setLocation(20, 20);

		shell.open();

		shell.setLayout(new FillLayout());
		Browser browser = new Browser(shell, SWT.NONE);

		shell.requestLayout();

		browser.setUrl("https://tlv8.com");

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		System.exit(0);
	}

}
