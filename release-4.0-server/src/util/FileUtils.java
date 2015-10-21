package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtils {

	public static void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	public static ArrayList<File> getFileListFlat(File source) {
		ArrayList<File> ret = new ArrayList<File>();

		if (source.isDirectory()) {
			File[] files = source.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					ret.addAll(getFileListFlat(files[i]));
				} else {
					ret.add(files[i]);
				}
			}
			return ret;
		} else {
			ret.add(source);
			return ret;
		}

	}
}
