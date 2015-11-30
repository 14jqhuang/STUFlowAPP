package other.bean;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import gui.alarm_dialog.WavFileFilter;
import resource.webserver.ResourcePath;

public class Musics implements ResourcePath {
	public HashMap< String, File> hashMap;
	public ArrayList<String> musicList;
	public String[] strMusicList;
	
	public Musics(String path) throws UnsupportedEncodingException {
		
		musicList=new ArrayList<String>();
		hashMap=new HashMap<String, File>();
		
		getMusic();
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private  void getMusicFile(int number) throws UnsupportedEncodingException
	{
		musicList=new ArrayList<String>();
		String temp;
		for(int i=0;i<number;i++)
		{
			temp=decode(ResourcePath.JARPATH)+i+".wav";
			musicList.add(temp);
		}
		strMusicList=new String[musicList.size()];
		musicList.toArray(strMusicList);
	}

	private void getMusic() throws UnsupportedEncodingException,NullPointerException
	{	
		File[] files=null;
		File f=new File(decode(JARPATH));
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
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		GetMusicFile m=new GetMusicFile();
//		new PlayMusic(m.musicList.get(0),false).play();
//	}

}
