package gui.alarm_dialog;

import java.io.File;
import java.io.FileFilter;

public class WavFileFilter implements FileFilter {

	public boolean accept(File file) {
		if(file.isDirectory())
			return false;
		else {
			String filename=file.getName();
			int index=filename.indexOf(".");
			if(filename.substring(index+1).equalsIgnoreCase("wav"))
				return true;
			else	return false;
		}
	}

}
