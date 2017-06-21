package com.dengjj.imageselector.album.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;


import com.dengjj.imageselector.album.bean.ImageFloder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public  class ATask extends AsyncTask<String, Integer, String> {
	private Context context;
	private ITaskCallBack iTaskCallBack;

	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();
	private int totalCount = 0;
	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;

	/**
	 * 扫描图片
	 * @param iTaskCallBack
     */
	public ATask(Context context, ITaskCallBack iTaskCallBack) {
		this.context = context;
		this.iTaskCallBack = iTaskCallBack;
	}

	@Override
	protected String doInBackground(String... params) {
		String firstImage = null;

		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = context.getContentResolver();

		// 只查询jpeg和png的图片
		Cursor mCursor = mContentResolver.query(mImageUri, null,
				MediaStore.Images.Media.MIME_TYPE + "=? or "
						+ MediaStore.Images.Media.MIME_TYPE + "=?",
				new String[] { "image/jpeg", "image/png" },
				MediaStore.Images.Media.DATE_MODIFIED);

		Log.e("TAG", mCursor.getCount() + "");
		while (mCursor.moveToNext())
		{
			// 获取图片的路径
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));

			Log.e("TAG", path);
			// 拿到第一张图片的路径
			if (firstImage == null)
				firstImage = path;
			// 获取该图片的父路径名
			File parentFile = new File(path).getParentFile();
			if (parentFile == null)
				continue;
			String dirPath = parentFile.getAbsolutePath();
			ImageFloder imageFloder = null;
			// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
			if (mDirPaths.contains(dirPath))
			{
				continue;
			} else
			{
				mDirPaths.add(dirPath);
				// 初始化imageFloder
				imageFloder = new ImageFloder();
				imageFloder.setDir(dirPath);
				imageFloder.setFirstImagePath(path);
			}
			if(parentFile.list()==null)continue;

			int picSize = parentFile.list(new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String filename)
				{
					if (filename.endsWith(".jpg")
							|| filename.endsWith(".png")
							|| filename.endsWith(".jpeg"))
						return true;
					return false;
				}
			}).length;
			totalCount += picSize;

			imageFloder.setCount(picSize);
			mImageFloders.add(imageFloder);

			if (picSize > mPicsSize)
			{
				mPicsSize = picSize;
				mImgDir = parentFile;
			}
		}
		mCursor.close();

		// 扫描完成，辅助的HashSet也就可以释放内存了
		mDirPaths = null;
		//扫描结果在全局变量中
		return null;



	}
	
	@Override
	protected void onPostExecute(String result) {
		iTaskCallBack.getResult(totalCount,mImageFloders,mPicsSize,mImgDir);
		super.onPostExecute(result);
	}

}
