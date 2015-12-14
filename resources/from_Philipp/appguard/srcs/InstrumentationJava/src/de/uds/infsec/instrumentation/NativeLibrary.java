package de.uds.infsec.instrumentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.os.Build;
import android.util.Log;

class NativeLibrary {

	public static final String TAG = "NativeLibrary";

	public static String extractNativeLibs(File apk, File libsDir) {
		try {
			// Copy libs for this architecture
			byte[] buf = new byte[1024];
			ZipInputStream zinstream = new ZipInputStream(new FileInputStream(apk));

			ZipEntry zentry = zinstream.getNextEntry();
			while (zentry != null) {
				String entryName = zentry.getName();
				if ((Build.CPU_ABI != null && entryName.startsWith("lib/" + Build.CPU_ABI))
				        || (Build.CPU_ABI2 != null && entryName.startsWith("lib/" + Build.CPU_ABI2))) {
					File out = new File(libsDir, entryName.substring(4));
					File parentFile = out.getParentFile();
					if (parentFile != null) {
						parentFile.mkdirs();
					}

					FileOutputStream outstream = new FileOutputStream(out);
					int n;
					while ((n = zinstream.read(buf)) != -1) {
						outstream.write(buf, 0, n);
					}
					outstream.close();
					zinstream.closeEntry();
				}
				zentry = zinstream.getNextEntry();
			}
			zinstream.close();

			// Build library lookup path
			StringBuilder libraryPath = new StringBuilder();
			if (Build.CPU_ABI != null) {
				File libDir1 = new File(libsDir, Build.CPU_ABI);
				if (libDir1.exists()) {
					libraryPath.append(libDir1.getAbsolutePath());

				}
			}
			if (Build.CPU_ABI2 != null) {
				File libDir2 = new File(libsDir, Build.CPU_ABI2);
				if (libDir2.exists()) {
					libraryPath.append(File.pathSeparatorChar);
					libraryPath.append(libDir2.getAbsolutePath());
				}
			}
			return libraryPath.toString();
		} catch (FileNotFoundException e) {
			Log.w(TAG, "Unable to extract monitor libraries. ", e);
		} catch (IOException e) {
			Log.w(TAG, "Unable to extract monitor libraries. ", e);
		}
		return null;
	}	

	public static void load(String libName, ClassLoader cl) {
		try {
			System.loadLibrary(libName);
		} catch (UnsatisfiedLinkError e) {
			String libPath = extractLibrary(libName, cl);
			if (libPath != null) {
				System.load(libPath);
			} else {
				Log.e(TAG, "Failed to load native lib " + libName);
			}
		}
	}
	
	private static String extractLibrary(String libName, ClassLoader cl) {
		InputStream lib = openLibrary(libName, cl);
		if (lib == null) {
			Log.e(TAG, "Failed to open library " + libName);
			return null;
		}

		try {
			String libPath = getLibraryPath(libName);
			OutputStream os = new FileOutputStream(libPath);

			int read = 0;
			byte[] buffer = new byte[4096];
			while ((read = lib.read(buffer)) > 0) {
				os.write(buffer, 0, read);
			}
			os.close();
			
			return libPath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static String getLibraryPath(String libName) {
		String dataDir = System.getenv("ANDROID_DATA");
		return dataDir + "/app-lib/lib" + libName + ".so";
	}

	private static InputStream openLibrary(String libName, ClassLoader cl) {
		final String[] abis = new String[] {
			Build.CPU_ABI,
			Build.CPU_ABI2
		};
		
		for (final String abi : abis) {
			String path = "lib/" + abi + "/lib" + libName + ".so";
			InputStream res = cl.getResourceAsStream(path);
			if (res != null) {
				return res;
			}
		}
		
		return null;
	}
}
