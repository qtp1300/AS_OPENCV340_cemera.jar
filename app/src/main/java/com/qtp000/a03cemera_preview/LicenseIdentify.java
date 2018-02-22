package com.qtp000.a03cemera_preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class LicenseIdentify
{
	/**
	 * 进行图片识别
	 *
	 * @param bitmap
	 *            待识别图片
	 * @param language
	 *            识别语言
	 * @return 识别结果字符串
	 */
	public String doOcr(Bitmap bitmap, String language)
	{
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.init(getSDPath(), language);
		System.gc();
		//必须加此行，tess-two要求BMP必须为此配置
		try{
		bitmap = bitmap.copy(Config.ARGB_8888, true);
		}catch(Exception e)
		{
			return null;
		}
		baseApi.setImage(bitmap);
		String text = baseApi.getUTF8Text();
		baseApi.clear();
		baseApi.end();
		return text;
	}
	/**
	 * 获取sd卡的路径
	 *
	 * @return 路径的字符串
	 */
	public static String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) 
		{
			sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
		}
		return sdDir.toString();
	}
	//对比度可调
	public Bitmap Contrast(Bitmap bit)
	{
		ColorMatrix cm = new ColorMatrix();
		float brightness = -25;  //亮度
		float contrast = 2;        //对比度
		cm.set(new float[] {
			contrast, 0, 0, 0, brightness,
			0, contrast, 0, 0, brightness,
			0, 0, contrast, 0, brightness,
			0, 0, 0, contrast, 0
		});
		Paint paint = new Paint(); 
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		//显示图片
		Matrix matrix = new Matrix();
		Bitmap createBmp = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), bit.getConfig());
		Canvas canvas = new Canvas(createBmp); 
		canvas.drawBitmap(bit, matrix, paint);
		
		return createBmp;
	}
	//不找出显示屏内部的封闭图形
	public Bitmap findcontourdraw(Bitmap bit)
	{
		Mat cannyEdges = new Mat();
		Mat grayMat = new Mat();
		Mat grayMat2 = new Mat();
		Mat grayMat3 = new Mat();
		Mat hierarchy = new Mat();
		Mat src = new Mat(bit.getHeight(),bit.getWidth(), CvType.CV_8UC4);
		Utils.bitmapToMat(bit, src);
		//建立存储像素点的集合
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//灰化
		Imgproc.GaussianBlur(src, grayMat3,new Size(3,3), 0);
		Imgproc.cvtColor(grayMat3, grayMat, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayMat, cannyEdges, 100, 100);
		Imgproc.dilate(cannyEdges, grayMat2, new Mat());
		//找出轮廓
		Imgproc.findContours(grayMat2, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		//获取包含最大轮廓的集合
		contours = Picarea(contours);
		//画线：这个是在显示屏的最外围话出你想要的大概的轮廓，
		//由于识别过程中不需要图片的显示，所以将其隐去
		for(int idx = 0;idx < contours.size(); idx++)
		{
			Scalar color = new Scalar(255,0,0);
			Imgproc.drawContours(src, contours, idx, color);
		}	
		MatOfPoint points = mJudgePic(contours);
		if(points == null)
			return null;
		CoordinatesPic(points);
		Mat pic = new Mat();
		try{						//竖							//横
			pic = src.submat(rowStart+20, rowEnd-6/*5到10*/, colStart+90, colEnd-10);
		}catch(CvException e){
			return null;
		}
		Bitmap bitm = Bitmap.createBitmap(pic.width(), pic.height(), Config.ARGB_8888);
		Utils.matToBitmap(pic, bitm);
		return bitm;
	}
	private List Picarea(List<MatOfPoint> con)
	{
		double temp1=0,S=0,temp2=0;
		for(int i=0;i < con.size();i++)
		{	
			S = Imgproc.contourArea(con.get(i));
			if(temp1 < S)
			{	
				temp1 = S;
				temp2 = i;
			}	
		}
		int y = (int)temp2;
		MatOfPoint x  = con.get(y);
		List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
		contour.add(x);
		return contour;
	}
	//判断封闭图形是否为矩形
	private MatOfPoint contour2;
	private long total = 0;
	//判断内部图形是否为封闭图形，是否为矩形
	private MatOfPoint contour3;
	private MatOfPoint mJudgePic(List<MatOfPoint> con)
	{
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		List<MatOfPoint> mcontour = new ArrayList<MatOfPoint>();
		
		MatOfPoint point = new MatOfPoint(con.get(0).toArray());
		MatOfPoint2f new_mat = new MatOfPoint2f(point.toArray());
		Imgproc.approxPolyDP(new_mat, approxCurve, 50, true);
		total = approxCurve.total();
		if(total == 4)
		{
			contour2 = new MatOfPoint(approxCurve.toArray());
			mcontour.add(contour2);
			return contour2;
		}
		return null;
	}
	//截取封闭图
	private double[] arr = new double[4];
	private int rowStart = 0;
	private int rowEnd = 0;
	private int colStart = 0;
	private int colEnd = 0;
	//获取顶点坐标
	private void CoordinatesPic(MatOfPoint mop)
	{	
		List<Point> pointlist = mop.toList();
		Point poi0 = pointlist.get(0);
		rowStart = (int) poi0.y;
		colStart= (int) poi0.x;
		
		Point poi2 = pointlist.get(2);
		rowEnd = (int) poi2.y;
		colEnd = (int) poi2.x;		
	}
	//将Mat类的图像处理完后需要重新设置其大小来适应bitmap这个位图
	private Mat displayMat(Mat pic,Bitmap bit)
	{
		int picW = bit.getWidth();
		int picH = bit.getHeight();
		Mat dsImage = new Mat(picH, picW, CvType.CV_8UC4);
		Imgproc.resize(pic, dsImage, dsImage.size());
		return dsImage;
	}
	//车牌的Mat转换成bitmap
	private Mat displayLicence(Mat pic)
	{
		int picW = rowEnd-rowStart;
		int picH = colEnd-colStart;
		Mat dsImage = new Mat(picH, picW, CvType.CV_8UC4);
		Imgproc.resize(pic, dsImage, dsImage.size());
		return dsImage;
	}
	//清晰处理
	public Bitmap LicenseShow(Bitmap bit)
	{
		Mat src = new Mat();
		Utils.bitmapToMat(bit, src);
		Mat cannyEdges = new Mat();
		Mat grayMat = new Mat();
		Mat erodeMat = new Mat();
		Imgproc.erode(src, grayMat,new Mat());
		//模糊处理
		Imgproc.GaussianBlur(grayMat, cannyEdges,new Size(3,3), 0);
		Imgproc.dilate(cannyEdges, erodeMat, new Mat());
			
		Utils.matToBitmap(erodeMat, bit);
		return bit;
	}
}
