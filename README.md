# chromium

1. Windows x86_64

pom
```
<dependency>
  <groupId>com.tlv8.chromium</groupId>
  <artifactId>tlv8.swt.browser.chromium.win32.win32.x86_64</artifactId>
  <version>1.0.0-RELEASE</version>
  <type>pom</type>
</dependency>
```

4.Use

```
import com.tulin.v8.swt.chromium.Browser;
public class SinglePagePart {
	public void createBrowser(Composite parent, String url) {
		Browser browser = new Browser(parent, SWT.NONE);
		browser.setUrl(url);
	}
}
```

