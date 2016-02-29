package lin.function;

import java.io.File;
import java.io.FileFilter;

public class WavFileFilter implements FileFilter {

	public boolean accept(File file) {
		// TODO Auto-generated method stub
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
