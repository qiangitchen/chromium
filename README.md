# chromium


1. pom

- Windows x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.win32.win32.x86_64</artifactId>
  <version>2.0.0</version>
</dependency>
```

- Linux x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.gtk.linux.x86_64</artifactId>
  <version>2.0.0</version>
</dependency>
```

- Mac OS x x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.macosx.x86_64</artifactId>
  <version>2.0.0</version>
</dependency>
```

2. Use

```
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
```

