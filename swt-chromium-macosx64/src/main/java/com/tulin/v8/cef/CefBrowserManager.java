package com.tulin.v8.cef;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefDownloadHandlerAdapter;
import org.cef.handler.CefJSDialogHandlerAdapter;
import org.cef.handler.CefLifeSpanHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.misc.BoolRef;

import com.tulin.v8.cef.handler.KeyboardHandler;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;

/**
 * CefBrowser管理
 * 
 * @author 陈乾
 */
public class CefBrowserManager {
	static final String userHomeDirectory = System.getProperty("user.home");
	static final String userLogsDirectory = userHomeDirectory + File.separator + "tulinv8" + File.separator + "logs";

	private static final CefAppBuilder builder = new CefAppBuilder();
	private static CefApp cefApp;
	private static String cachePath;

	/**
	 * JCEF初始化
	 */
	static {
		CefSettings cefSettings = builder.getCefSettings();
		cefSettings.windowless_rendering_enabled = false;
		cefSettings.locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
		String installDir = userHomeDirectory + File.separator + ".jcef";
		cefSettings.resources_dir_path = installDir;
		String tempDirectory = System.getProperty("java.io.tmpdir");
		cachePath = tempDirectory + File.separator + ".jcef-cache" + File.separator + UUID.randomUUID().toString();
		cefSettings.cache_path = cachePath;
		cefSettings.log_file = userLogsDirectory + File.separator + "cef.log";
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
		client.addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
			@Override
			public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String target_url,
					String target_frame_name) {
				new BrowserFrame(target_url, useOSR, isTransparent).open();
				return true;// 返回true表示取消弹出窗口
			}
		});
		// 处理键盘事件
		client.addKeyboardHandler(new KeyboardHandler());
		// 处理下载事件
		client.addDownloadHandler(new CefDownloadHandlerAdapter() {
			@Override
			public boolean onBeforeDownload(CefBrowser browser, CefDownloadItem downloadItem, String suggestedName,
					CefBeforeDownloadCallback callback) {
				callback.Continue(null, true);
				return true;// 返回true表示取消弹出窗口
			}
		});
		// 处理JS消息提示框
		client.addJSDialogHandler(new CefJSDialogHandlerAdapter() {
			@Override
			public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type,
					String message_text, String default_prompt_text, CefJSDialogCallback callback,
					BoolRef suppress_message) {
				if (dialog_type == JSDialogType.JSDIALOGTYPE_ALERT) {
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(browser.getUIComponent(), message_text, "提示",
								JOptionPane.INFORMATION_MESSAGE);
						callback.Continue(true, null);
					});
				} else if (dialog_type == JSDialogType.JSDIALOGTYPE_CONFIRM) {
					SwingUtilities.invokeLater(() -> {
						int c = JOptionPane.showConfirmDialog(browser.getUIComponent(), message_text, "确认",
								JOptionPane.OK_CANCEL_OPTION);
						callback.Continue(c == JOptionPane.OK_OPTION, null);
					});
				} else {
					return false;
				}
				return true;
			}
		});
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

	public static void release() {
		if (cefApp != null) {
			cefApp.dispose();
		}
		if (cachePath != null) {
			try {
				File t = new File(cachePath);
				if (t.exists()) {
					deleteFolder(t);
				}
			} catch (Exception ignored) {
			}
		}
	}

	public static void deleteFolder(File folder) {
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					file.delete();
				} else {
					deleteFolder(file);
				}
			}
			folder.delete();
		} else {
			folder.delete();
		}
	}
}
