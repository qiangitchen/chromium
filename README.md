# chromium


1. pom

- Windows x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.win32.win32.x86_64</artifactId>
  <version>1.0.0-RELEASE</version>
</dependency>
```

- Linux x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.gtk.linux.x86_64</artifactId>
  <version>1.0.0-RELEASE</version>
</dependency>
```

- Mac OS x x86_64
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.macosx.x86_64</artifactId>
  <version>1.0.0-RELEASE</version>
</dependency>
```

2. Use

```
import com.tulin.v8.swt.chromium.Browser;
public class SinglePagePart {
	public void createBrowser(Composite parent, String url) {
		Browser browser = new Browser(parent, SWT.NONE);
		browser.setUrl(url);
	}
}
```

