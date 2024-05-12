package com.tulin.v8.cef;

import java.io.File;
import java.util.Locale;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefLoadHandlerAdapter;

import com.tulin.v8.cef.handler.KeyboardHandler;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

/**
 * CefBrowser管理
 * 
 * @author 陈乾
 */
public class CefBrowserManager {
	private static final CefAppBuilder builder = new CefAppBuilder();
	private static CefApp cefApp;

	/**
	 * JCEF初始化
	 */
	static {
		CefSettings cefSettings = builder.getCefSettings();
		cefSettings.windowless_rendering_enabled = false;
		cefSettings.locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
		String userHomeDirectory = System.getProperty("user.home");
		String installDir = userHomeDirectory + File.separator + ".jcef";
		cefSettings.resources_dir_path = installDir;
		cefSettings.cache_path = userHomeDirectory + File.separator + ".jcef-cache";
		//cefSettings.root_cache_path = userHomeDirectory + File.separator + ".jcef-cache";
		builder.setInstallDir(new File(installDir));
	}

	public static CefApp getCefApp() {
		return cefApp;
	}

	public static void init(MavenCefAppHandlerAdapter vavenCefAppHandlerAdapter) throws Exception {
		cefApp = builder.build();
		// Must be called before CefApp is initialized
		builder.setAppHandler(vavenCefAppHandlerAdapter);
	}

	/**
	 * 创建新的浏览器
	 * 
	 * @param startURL
	 * @param useOSR
	 * @param isTransparent
	 * @return
	 */
	public static CefBrowser createCefBrowser(String startURL, boolean useOSR, boolean isTransparent) {
		if (cefApp == null) {
			try {
				init(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CefClient client = cefApp.createClient();
		// 处理弹出窗口
		//client.addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
		//	@Override
		//	public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url,
		//			String target_frame_name) {
		//		new BrowserFrame(target_url, useOSR, isTransparent).open();
		//		return true;// 返回true表示取消弹出窗口
		//	}
		//});
		// 处理键盘事件
		client.addKeyboardHandler(new KeyboardHandler());
		// 处理浏览器页面加载状态的变化
		client.addLoadHandler(new CefLoadHandlerAdapter() {
			@Override
			public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
				// System.out.println("httpStatusCode:" + httpStatusCode);
			}

			@Override
			public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText,
					String failedUrl) {
				// System.err.println("errorCode:" + errorCode.getCode());
				// System.err.println("errorText:" + errorText);
				// System.err.println("failedUrl:" + failedUrl);
				// String p404 = "file://" + SourceUtils.getSourceAbsPath("/pages/404.html");
				// browser.loadURL(p404);
			}
		});
		return client.createBrowser(startURL, useOSR, isTransparent);
	}
}
