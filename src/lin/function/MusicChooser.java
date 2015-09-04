package lin.function;

import javax.swing.JFileChooser;

@SuppressWarnings("serial")
@Deprecated
public class MusicChooser extends JFileChooser {
	private WavFileFliter wavFilter;
	public MusicChooser() {
		// TODO Auto-generated constructor stub
		super();
		wavFilter=new WavFileFliter();
		this.setFileFilter(wavFilter);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
