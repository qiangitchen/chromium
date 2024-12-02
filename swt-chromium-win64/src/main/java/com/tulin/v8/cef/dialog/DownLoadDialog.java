package com.tulin.v8.cef.dialog;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 文件下载对话框
 */
public class DownLoadDialog extends Dialog {
	private Label messageLabel;
	private String message;
	private String fullPath;

	public DownLoadDialog(Shell parent) {
		super(parent);
	}

	public void setMessage(String message) {
		this.message = message;
		if (messageLabel != null && message != null) {
			messageLabel.setText(message);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("文件下载");
		Composite composite = new Composite(parent, SWT.NONE);
		messageLabel = new Label(composite, SWT.NONE);
		if (message != null) {
			messageLabel.setText(message);
		}
		return composite;
	}

	public boolean isDisposed() {
		return getShell() == null || getShell().isDisposed();
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "打开", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "关闭", false);
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			if (fullPath != null && !"".equals(fullPath)) {
				try {
					Desktop.getDesktop().open(new File(fullPath));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
