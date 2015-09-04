package lin.readwrite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import lin.function.WavFileFilter;

public class GetMusicFile {
	public HashMap< String, File> hashMap;
	public ArrayList<String> musicList;
	public String[] strMusicList;
	public GetMusicFile(String path) {
		// TODO Auto-generated constructor stub
		musicList=new ArrayList<String>();
		hashMap=new HashMap<String, File>();
		getMusic(path);
	}
	
@Deprecated
	public void getMusicFile(int number)
	{
		musicList=new ArrayList<String>();
		String temp;
		for(int i=0;i<number;i++)
		{
			temp=ResourcePath.JARPATH+i+".wav";
			musicList.add(temp);
		}
		strMusicList=new String[musicList.size()];
		musicList.toArray(strMusicList);
	}
	public void getMusic(String jarPath)
	{	
		try {
			File[] files=null;
			File f=new File(jarPath);
			if(f.isDirectory())
				files=f.listFiles(new WavFileFilter());
			for(File fi:files)
			{
				String path=fi.getAbsolutePath();
				String name=path.substring(path.lastIndexOf("\\")+1);
				musicList.add(name);
				hashMap.put(name, fi);
			}
		
				strMusicList=new String[musicList.size()];//String[] a=new String[0];这种形式是不会报错的 会分配一个空间
																			//试图输出的元素的话会报下标越界
				musicList.toArray(strMusicList);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
//System.out.println("没有音乐文件");
		}			
	}
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		GetMusicFile m=new GetMusicFile();
//		new PlayMusic(m.musicList.get(0),false).play();
//	}

}
