package com.tulin.v8.web.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.tulin.v8.web.enums.NSSystemProperty;

public class Utils {
	public static final boolean IS_JAVA_6_OR_GREATER = SystemProperty.JAVA_VERSION.get().compareTo("1.6") >= 0;
	public static final boolean IS_JAVA_7_OR_GREATER = SystemProperty.JAVA_VERSION.get().compareTo("1.7") >= 0;
	public static final boolean IS_MAC;
	public static final boolean IS_WINDOWS;
	public static final boolean IS_32_BIT;
	public static final boolean IS_64_BIT;
	public static final boolean IS_WEBSTART = SystemProperty.JAVAWEBSTART_VERSION.get() != null;
	public static final boolean IS_WINDOWS_VISTA_OR_GREATER;
	public static final boolean IS_WINDOWS_7_OR_GREATER;
	public static final String LINE_SEPARATOR;
	private static String localHostAddress;

	static {
		String os = SystemProperty.OS_NAME.get();
		IS_MAC = (os.startsWith("Mac")) || (os.startsWith("Darwin"));
		IS_WINDOWS = os.startsWith("Windows");
		String arch = SystemProperty.OS_ARCH.get();
		IS_64_BIT = ("x86_64".equals(arch)) || ("x64".equals(arch)) || ("amd64".equals(arch)) || ("ia64".equals(arch))
				|| ("ppc64".equals(arch)) || ("IA64N".equals(arch))
				|| ("64".equals(SystemProperty.SUN_ARCH_DATA_MODEL.get()))
				|| ("64".equals(SystemProperty.COM_IBM_VM_BITMODE.get()));
		IS_32_BIT = !IS_64_BIT;

		IS_WINDOWS_VISTA_OR_GREATER = (IS_WINDOWS) && (SystemProperty.OS_VERSION.get().compareTo("6.0") >= 0);
		IS_WINDOWS_7_OR_GREATER = (IS_WINDOWS) && (SystemProperty.OS_VERSION.get().compareTo("6.1") >= 0);

		LINE_SEPARATOR = SystemProperty.LINE_SEPARATOR.get();
	}

	public static String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encodeURL(String s) {
		String encodedString;
		try {
			encodedString = URLEncoder.encode(s, "UTF-8");
		} catch (Exception e) {
			encodedString = s;
		}
		return encodedString.replaceAll("\\+", "%20");
	}

	public static String encodeBase64(String s, boolean isURLSafe) {
		return Base64.encode(s, isURLSafe);
	}

	public static String decodeBase64(String s) {
		return Base64.decode(s);
	}

	public static String escapeXML(String s) {
		if ((s == null) || (s.length() == 0)) {
			return s;
		}
		StringBuilder sb = new StringBuilder((int) (s.length() * 1.1D));
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			default:
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static File getLocalFile(String path) {
		if (path == null) {
			return null;
		}
		if (path.startsWith("file:")) {
			File file = new File(decodeURL(path.substring("file:".length())));
			if (file.exists()) {
				return simplifyLocalFile(file);
			}
		}
		File file = new File(path);
		if (file.exists()) {
			return simplifyLocalFile(file);
		}
		return null;
	}

	private static File simplifyLocalFile(File localFile) {
		try {
			File cFile = localFile.getCanonicalFile();
			if (cFile.exists())
				return cFile;
		} catch (Exception localException) {
		}
		return localFile;
	}

	public static File getClassPathFile(String resourcePath) {
		File file = getJARFile(resourcePath);
		return file != null ? file : getDirectory(resourcePath);
	}

	public static File getClassPathFile(Class<?> clazz) {
		File file = getJARFile(clazz);
		return file != null ? file : getDirectory(clazz);
	}

	public static File getJARFile(String resourcePath) {
		if (!resourcePath.startsWith("/")) {
			resourcePath = '/' + resourcePath;
		}
		return getJARFile(Utils.class, resourcePath);
	}

	public static File getJARFile(Class<?> clazz) {
		return getJARFile(clazz, "/" + clazz.getName().replace('.', '/') + ".class");
	}

	private static File getJARFile(Class<?> clazz, String resourcePath) {
		URL resource = clazz.getResource(resourcePath);
		if (resource == null) {
			return null;
		}
		String classResourceURL = resource.toExternalForm();
		if ((classResourceURL != null) && (classResourceURL.startsWith("jar:file:"))) {
			classResourceURL = classResourceURL.substring("jar:file:".length());
			if (classResourceURL.endsWith("!" + resourcePath)) {
				return new File(decodeURL(classResourceURL
						.substring(0, classResourceURL.length() - 1 - resourcePath.length()).replace("+", "%2B")));
			}
		}
		return null;
	}

	public static File getDirectory(String resourcePath) {
		if (!resourcePath.startsWith("/")) {
			resourcePath = '/' + resourcePath;
		}
		return getDirectory(Utils.class, resourcePath);
	}

	public static File getDirectory(Class<?> clazz) {
		return getDirectory(clazz, "/" + clazz.getName().replace('.', '/') + ".class");
	}

	private static File getDirectory(Class<?> clazz, String resourcePath) {
		String resourceName = resourcePath;
		if (resourceName.startsWith("/")) {
			resourceName = resourceName.substring(1);
		}
		URL resource = clazz.getResource(resourcePath);
		if (resource == null) {
			return null;
		}
		String classResourceURL = resource.toExternalForm();
		if ((classResourceURL != null) && (classResourceURL.startsWith("file:"))) {
			File dir = new File(decodeURL(classResourceURL.substring("file:".length()))).getParentFile();
			for (int i = 0; i < resourceName.length(); i++) {
				if (resourceName.charAt(i) == '/') {
					dir = dir.getParentFile();
				}
			}
			return dir;
		}
		return null;
	}

	public static void deleteAll(File fileOrDir) {
		if ((!fileOrDir.delete()) && (fileOrDir.isDirectory())) {
			for (File file : fileOrDir.listFiles()) {
				deleteAll(file);
			}
			fileOrDir.delete();
		}
	}

	public static boolean equals(Object o1, Object o2) {
		return o1 == null ? false : o2 == null ? true : o1.equals(o2);
	}

	@SuppressWarnings("rawtypes")
	public static String arrayDeepToString(Object array) {
		if (array == null) {
			return null;
		}
		Class clazz = array.getClass();
		if (!clazz.isArray()) {
			return null;
		}
		if (clazz == Boolean.class) {
			return Arrays.toString((boolean[]) array);
		}
		if (clazz == Byte.class) {
			return Arrays.toString((byte[]) array);
		}
		if (clazz == Short.class) {
			return Arrays.toString((short[]) array);
		}
		if (clazz == Character.class) {
			return Arrays.toString((char[]) array);
		}
		if (clazz == Integer.class) {
			return Arrays.toString((int[]) array);
		}
		if (clazz == Long.class) {
			return Arrays.toString((long[]) array);
		}
		if (clazz == Float.class) {
			return Arrays.toString((float[]) array);
		}
		if (clazz == Double.class) {
			return Arrays.toString((double[]) array);
		}
		return Arrays.deepToString((Object[]) array);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String simplifyPath(String path) {
		if (path.indexOf("//") != -1) {
			throw new IllegalArgumentException("The path is invalid: " + path);
		}
		String[] crumbs = path.split("/");
		List crumbList = new ArrayList(crumbs.length);
		for (String crumb : crumbs) {
			if (("".equals(crumb)) || (".".equals(crumb)))
				continue;
			if ("..".equals(crumb)) {
				int index = crumbList.size() - 1;
				if (index == -1) {
					throw new IllegalArgumentException("The path is invalid: " + path);
				}
				crumbList.remove(index);
			} else {
				crumbList.add(crumb);
			}
		}
		StringBuilder sb = new StringBuilder(path.length());
		if (path.startsWith("/")) {
			sb.append('/');
		}
		int crumbCount = crumbList.size();
		for (int i = 0; i < crumbCount; i++) {
			if (i > 0) {
				sb.append('/');
			}
			sb.append((String) crumbList.get(i));
		}
		if ((path.length() > 1) && (path.endsWith("/"))) {
			sb.append('/');
		}
		return sb.toString();
	}

	public static void printStackTraces() {
		printStackTraces(System.err);
	}

	public static void printStackTraces(PrintStream printStream) {
		printStream.print(getStackTracesAsString());
	}

	public static void printStackTraces(PrintWriter printWriter) {
		printWriter.print(getStackTracesAsString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getStackTracesAsString() {
		Map allStackTraces = Thread.getAllStackTraces();
		Thread[] threads = (Thread[]) allStackTraces.keySet().toArray(new Thread[0]);
		Arrays.sort(threads, new Comparator<Thread>() {
			public int compare(Thread o1, Thread o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		StringBuilder sb = new StringBuilder();
		for (Thread t : threads) {
			sb.append(t.isDaemon() ? "Daemon Thread [" : "Thread [").append(t.getName()).append("] (")
					.append(t.getState()).append(")").append(LINE_SEPARATOR);
			StackTraceElement[] stackTraceElements = (StackTraceElement[]) allStackTraces.get(t);
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				sb.append("\tat ").append(stackTraceElement).append(LINE_SEPARATOR);
			}
		}
		return sb.toString();
	}

	public static String getLocalHostAddress() {
		synchronized (Utils.class) {
			if (localHostAddress != null) {
				return "".equals(localHostAddress) ? null : localHostAddress;
			}
			String localHostAddress = NSSystemProperty.LOCALHOSTADDRESS.get();
			if ("_localhost_".equals(localHostAddress)) {
				try {
					localHostAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (Exception e) {
					localHostAddress = null;
				}
			}
			if (localHostAddress == null) {
				boolean isDebugging = Boolean
						.parseBoolean(NSSystemProperty.LOCALHOSTADDRESS_DEBUG_PRINTDETECTION.get());
				localHostAddress = getLocalHostAddress(0, isDebugging);
			}
			if (Boolean.parseBoolean(NSSystemProperty.LOCALHOSTADDRESS_DEBUG_PRINT.get())) {
				System.err.println("Local host address: " + localHostAddress);
			}
			localHostAddress = localHostAddress == null ? "" : localHostAddress;
			return localHostAddress;
		}
	}

	public static String getLocalHostAddress(int port) {
		return getLocalHostAddress(port, false);
	}

	@SuppressWarnings("rawtypes")
	private static String getLocalHostAddress(int port, boolean isDebugging) {
		if (isDebugging) {
			System.err.println("Local host address detection using "
					+ (port == 0 ? "an automatic port" : new StringBuilder("port ").append(port).toString()) + ":");
		}
		String loopbackAddress = "127.0.0.1";
		if (isDebugging) {
			System.err.print("  Trying 127.0.0.1: ");
		}
		if (isLocalHostAddressReachable(loopbackAddress, port)) {
			if (isDebugging) {
				System.err.println("success.");
			}
			return loopbackAddress;
		}
		if (isDebugging) {
			System.err.println("failed.");
		}
		List<InetAddress> inetAddressList = new ArrayList<>();
		NetworkInterface networkInterface;
		try {
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				networkInterface = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = networkInterface.getInetAddresses(); en2.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) en2.nextElement();
					if (!loopbackAddress.equals(inetAddress.getHostAddress()))
						inetAddressList.add(inetAddress);
				}
			}
		} catch (SocketException localSocketException) {
		}
		Collections.sort(inetAddressList, new Comparator<InetAddress>() {
			public int compare(InetAddress o1, InetAddress o2) {
				if ((o1.isLoopbackAddress() != o2.isLoopbackAddress())
						&& (o1.isSiteLocalAddress() != o2.isSiteLocalAddress())) {
					if (o1.isLoopbackAddress()) {
						return -1;
					}
					if (o2.isLoopbackAddress()) {
						return 1;
					}
					if (o1.isSiteLocalAddress()) {
						return -1;
					}
					if (o2.isSiteLocalAddress()) {
						return 1;
					}
				}
				return o1.getHostAddress().compareTo(o2.getHostAddress());
			}
		});
		if (isDebugging) {
			System.err.println("  Trying addresses: " + inetAddressList);
		}
		for (InetAddress address : inetAddressList) {
			String hostAddress = address.getHostAddress();
			if (isDebugging) {
				System.err.print("    " + hostAddress + ": ");
			}
			if (isLocalHostAddressReachable(hostAddress, port)) {
				if (isDebugging) {
					System.err.println("success.");
				}
				return hostAddress;
			}
			if (isDebugging)
				System.err.println("failed.");
		}
		try {
			if (isDebugging) {
				System.err.print("  Trying LocalHost: ");
			}

			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			if (isDebugging) {
				System.err.print("success (" + hostAddress + ").");
			}
			return hostAddress;
		} catch (Exception localException) {
			if (isDebugging) {
				System.err.println("failed.");
				System.err.println("  Failed to find a suitable local host address!");
			}
		}
		return null;
	}

	private static boolean isLocalHostAddressReachable(String hostAddress, int port) {
		boolean isReachable = false;
		try {
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(InetAddress.getByName(hostAddress), port));
			port = serverSocket.getLocalPort();
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(hostAddress, port), 500);
				isReachable = true;
				socket.close();
			} catch (Exception e) {
				try {
					serverSocket.close();
				} catch (IOException localIOException) {
				}
			}
			serverSocket.close();
		} catch (Exception localException1) {
		}
		return isReachable;
	}
}
