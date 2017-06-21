package com.dengjj.imageselector.album.task;


import com.dengjj.imageselector.album.bean.ImageFloder;

import java.io.File;
import java.util.List;

public interface ITaskCallBack {
	
	public void getResult(int totalCount,
						  List<ImageFloder> mImageFloders,
						  int mPicsSize,
						  File mImgDir);
	

}
