package com.tulin.v8.cef;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefDisplayHandlerAdapter;

/**
 * 浏览器窗口
 * 
 * @author 陈乾
 */
public class BrowserFrame extends JFrame {
	private static final long serialVersionUID = 7366705992856664735L;
	private final CefClient client_;
	private final CefBrowser browser_;
	private final Component browerUI_;

	public BrowserFrame(String startURL, boolean useOSR, boolean isTransparent) {
		browser_ = CefBrowserManager.createCefBrowser(startURL, false, false);
		client_ = browser_.getClient();
		addHandler();
		browerUI_ = browser_.getUIComponent();
		getContentPane().add(browerUI_, BorderLayout.CENTER);
	}

	public BrowserFrame(String startURL) {
		this(startURL, false, false);
	}

	private void addHandler() {
		client_.removeDisplayHandler();
		// 处理标题地址更新等
		client_.addDisplayHandler(new CefDisplayHandlerAdapter() {
			@Override
			public void onTitleChange(CefBrowser browser, String title) {
				setTitle(title);
			}
		});
	}

	public void open() {
		pack();
		setTitle("浏览器");
		setSize(1400, 800);
		setLocationRelativeTo(null);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				client_.dispose();
				dispose();
			}
		});
	}
}
