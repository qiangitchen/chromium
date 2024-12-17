# chromium


1. pom

- Windows x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.win32.win32.x86_64</artifactId>
  <version>2.2.0</version>
</dependency>
```

- Windows arm64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.win.arm64</artifactId>
  <version>2.2.0</version>
</dependency>
```

- Linux x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.gtk.linux.x86_64</artifactId>
  <version>2.2.0</version>
</dependency>
```

- Linux arm64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.gtk.linux.arm64</artifactId>
  <version>2.2.0</version>
</dependency>
```

- Mac OS x x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.macosx.x86_64</artifactId>
  <version>2.2.0</version>
</dependency>
```

- Mac OS x arm64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.macosx.aarch64</artifactId>
  <version>2.2.0</version>
</dependency>
```

2. Use

```
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
```

