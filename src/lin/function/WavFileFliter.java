package lin.function;

import java.io.File;

import javax.swing.filechooser.FileFilter;
@Deprecated
public class WavFileFliter extends FileFilter {

	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		if(f.isDirectory())
			return false;
		String filename=f.getName();
		int index=filename.indexOf(".");
		if(filename.substring(index+1).equalsIgnoreCase("wav"))
			return true;
		else	return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "“Ù¿÷Œƒº˛(.wav)";
	}

}
