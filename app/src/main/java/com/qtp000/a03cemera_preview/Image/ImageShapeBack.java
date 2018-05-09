package com.qtp000.a03cemera_preview.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
//import org.opencv.core.Core.MinMaxLocResult;
//import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.utils.Converters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.qtp000.a03cemera_preview.EmitContur;
import com.qtp000.a03cemera_preview.Image.ColorBlobDetector;

public class ImageShapeBack {
	public class ImageInfo{
		public int pointCoint;
		public int color;
		public int type;
	}
	Mat Canny_bluMat = new Mat();
	Mat Canny_cannyMat = new Mat();
	Mat Canny_grayMat = new Mat();
	Mat Canny_alpMat = new Mat();
	 Mat Canny_grayMatE = new Mat(); 
	 Mat Canny_bluMat1 = new Mat();
		Mat Canny_cannyMat1 = new Mat();
		Mat Canny_grayMat1 = new Mat();
		Mat Canny_alpMat1 = new Mat();
		 Mat Canny_grayMatE1 = new Mat(); 
	 
	 Mat findImageTrue_CannyMat = new Mat(); 
	 Mat srcImageMat = null;
	 Mat dstImageMat = null;
	 Mat warpPersMatMat = new Mat();
	private Bitmap bitmap = null;
	private int backColor = Color.BLACK;
	private List<EmitContur> emitConturList =  new ArrayList<EmitContur>();
    private ColorBlobDetector mDetector;
    private List<ImageInfo>  imageInfoList = new ArrayList<ImageInfo>();
    
	public List<ImageInfo> getImageInfoList() {
		return imageInfoList;
	}
	public List<EmitContur> getEmitConturList() {
		return emitConturList;
	}
	public ImageShapeBack(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
		//BimapFindContours();
	}
	public void ImageBackRun()
	{
		Mat imageMat = findImageTrue(bitmap);
		if(imageMat == null)
			return;
		imageInfoList = CheckImage(imageMat);
		//displayMat(imageMat);
	}
	public void ImageBackRun(Mat imageMat)
	{
		if(imageMat == null)
			return;
		imageInfoList = CheckImage(imageMat);
		//displayMat(imageMat);
	}
	public Bitmap getBitmap() {
		//bitmap = readPImage("/sdcard/back.jpg");
		return bitmap;
	}
	public int getBackColor() {
		return backColor;
	}
	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}
	//����ͼ���Ե����
	private Mat getCannyMat(Mat src)
	{
		double pointD = 1;
		double point2D =2;
		
		 List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		 MatOfPoint matPoint = new MatOfPoint(new Point(pointD,pointD),new Point(pointD,src.size().height-pointD),new Point(src.size().width-point2D,src.size().height-point2D),new Point(src.size().width-point2D,point2D));

		 contours.add(matPoint);
			Imgproc.cvtColor(src, Canny_cannyMat1, Imgproc.COLOR_BGR2GRAY);
			Imgproc.drawContours( Canny_cannyMat1, contours, -1, new Scalar(255,255,255),2);
			Imgproc.Canny(Canny_cannyMat1, Canny_cannyMat1, 100, 20);
			Imgproc.dilate(Canny_cannyMat1, Canny_grayMatE1, new Mat());

         return Canny_grayMatE1;

	}
	private Mat getTrueCannyMat(Mat src)
	{
		Scalar scalar = getRectScaleFromeMat(src,new Rect(src.width()/2,10,4,4));
		// Mat Canny_grayMatE = new Mat(); 
		int rgbR  = (int) scalar.val[0];
		int rgbG  = (int) scalar.val[1];
		int rgbB = (int) scalar.val[2];
		int rgbSum = (rgbR + rgbG + rgbB)/3;
		if(Math.abs(rgbR - rgbSum)<20 &&Math.abs(rgbG - rgbSum)<20&&Math.abs(rgbG - rgbSum)<20)
		{
			int R  = rgbR > 80?255:0;
			int G  = rgbR > 80?255:0;
			int B =  rgbR > 80?255:0;
			rgbR = R;
			rgbG = G;
			rgbB = B;
		}else
		{
			if(rgbR >= rgbSum)
				rgbR = 255;
			else
				rgbR = 0;
			if(rgbG >= rgbSum)
				rgbG = 255;
			else
				rgbG = 0;
			if(rgbB >= rgbSum)
				rgbB = 255;
			else
				rgbB = 0;
		}
		int color = Color.argb(0, rgbR, rgbG, rgbB);
		int minF = 50,maxF = 5;
		if(color == 0xffffff)
		{
			Mat srcMat = new Mat();
			Mat graymat = new Mat();
			
			minF =5;
			maxF =5;
			
//			minF =5;
//			maxF =5;
			
			//Mat Canny_bluMat = new Mat();
			//Mat Canny_cannyMat = new Mat();
			//Mat Canny_grayMat = new Mat();
			src.convertTo(Canny_alpMat, -1,1.5/*����*/,50/*�Աȶ�*/);				//�����ʶ�𲻳���������Ҷ�ֵ
			//Imgproc.blur(Canny_alpMat, Canny_bluMat, new Size(3,3));
			//Imgproc.blur(src, Canny_bluMat, new Size(3,3));
			 //Imgproc.dilate(src, src, new Mat(),new Point(-1,-1),2);
			 Imgproc.cvtColor(Canny_alpMat, Canny_grayMat, Imgproc.COLOR_BGR2GRAY);
			 Imgproc.Canny(Canny_grayMat, srcMat, minF, maxF);
			 Imgproc.dilate(srcMat, graymat, new Mat(),new Point(-1,-1),1);
			 
			 Imgproc.dilate(graymat, Canny_grayMatE, new Mat(),new Point(-1,-1),1);
			 
			 //Imgproc.blur(Canny_grayMatE, Canny_grayMatE, new Size(2,2));
		}else if(color == 0x000000)
		{
			minF =100;
			maxF =5;
			//Mat Canny_bluMat = new Mat();
			//Mat Canny_cannyMat = new Mat();
			//Mat Canny_grayMat = new Mat();
			//Mat Canny_alpMat = new Mat();
			src.convertTo(Canny_alpMat, -1,2.5,50);
			 Imgproc.blur(Canny_alpMat, Canny_bluMat, new Size(3,3));
			 Imgproc.cvtColor(Canny_bluMat, Canny_grayMat, Imgproc.COLOR_BGR2GRAY);
			 Imgproc.Canny(Canny_grayMat, Canny_cannyMat, minF, maxF);
			 Imgproc.blur(Canny_cannyMat, Canny_grayMatE, new Size(3,3));
		}else
		{
			minF =50;
			maxF =5;
			Mat bluMat = new Mat();
			Mat cannyMat = new Mat();
			Mat grayMat = new Mat();
			Imgproc.blur(src, bluMat, new Size(3,3));
			 Imgproc.cvtColor(bluMat, grayMat, Imgproc.COLOR_BGR2GRAY);
			 Imgproc.Canny(grayMat, cannyMat, minF, maxF);
			 Imgproc.blur(cannyMat, Canny_grayMatE, new Size(3,3));
		}
         return Canny_grayMatE;
	}
	private MatOfPoint findContoursFromGrayMat(Mat grayMat)
	{
		//摄像头区域的十分之一
//		double mMinContourArea = 0.05;
		double mMinContourArea = 0.01;
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//找出传入图片的所有封闭图形的轮廓
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		//定义图片的的最大尺寸
		double maxAr =320*280*0.95;
		double maxArea = 0;
		double mixArea = 0;
		//遍历所有轮廓的集合
		Iterator<MatOfPoint> each = contours.iterator();
		while (each.hasNext()) 
		{
			MatOfPoint wrapper = each.next();
			double area = Imgproc.contourArea(wrapper);
			if (area > maxArea)//找出最大的轮廓集合
				maxArea = area;
	            mixArea = maxArea;
		}
		List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
		each = contours.iterator();
		while (each.hasNext()) 
		{
			MatOfPoint contour = each.next();
			double area = Imgproc.contourArea(contour);
			//如果轮廓的面积大于摄像头获取图片面积的十分之一并且小于最大轮廓面积
			if ( area > mMinContourArea*maxArea &&area <maxAr) 
			{
				mContours.add(contour);
			}
		}
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
		each = mContours.iterator();
		while (each.hasNext()) //判断得到的轮廓的集合是否是一个四边形
		{
			MatOfPoint contour = each.next();
			MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );
			Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
			long total  = approxCurve.total();
			if (total == 4 ) 
			{
				MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());
				mContour2.add(contour2);
			}
		}
		Iterator<MatOfPoint> each2 = mContour2.iterator();
		MatOfPoint mContour3 = null;
		while (each2.hasNext()) 
		{
			MatOfPoint wrapper = each2.next();
			double area = Imgproc.contourArea(wrapper);
			List<Point> pointList = wrapper.toList();
			//得到最后想要图片的大小
			double width =  Math.abs(pointList.get(2).x - pointList.get(0).x);
			double height =  Math.abs(pointList.get(2).y - pointList.get(0).y);
			float b1 = ((float) width/(float) height);    
			if( b1 > 1 &&b1 < 2)
			{
				if (area < mixArea )
				{
					mixArea = area;
					mContour3 = wrapper;
		        }
			}
		} 
		return mContour3;
	}
	private List<MatOfPoint> findEmitFromGrayMat(Mat grayMat)
	{
		double mMinContourArea = 0.01;
		double mMaxContourArea = 0.1;
		
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		double maxArea = grayMat.height()*grayMat.width();
	        Iterator<MatOfPoint> each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint wrapper = each.next();
	            double area = Imgproc.contourArea(wrapper);
	            if (area > maxArea )
	                maxArea = area;
	        }
	        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
	        int size = contours.size();
	        for(int i = 0;i< size;i++)
	        {
	        	MatOfPoint contour = contours.get(i);
	         double area = Imgproc.contourArea(contour);
		            if (area > mMinContourArea*maxArea &&area< mMaxContourArea*maxArea) 
	         //if (area > 2 ) 
		            {
		            	
		               
		                double[] m1 = hierarchy.get(0, i);
		                if(m1[2] != -1 && m1[3] == -1)
		                {
		                	 MatOfPoint2f approxCurve = new MatOfPoint2f();
		                 
		                	 MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );
		 	            	Imgproc.approxPolyDP(new_mat, approxCurve,10, true);//���Ҳ��Ҫ�����������Ƿ�����Ƿ�ƽ��
		 	            	MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());
		 	            	mContours.add(contour2);
		                }
		                
		            }
	        }
		return mContours;
	}
	private Rect getImageTrueRect2(MatOfPoint matPoint)
	{
		double pointD = 1;
		//获取把坐标轮廓变成数组变成数组形式
		Point[] poitI = matPoint.toArray();
		Point[] poitII = matPoint.toArray();
		
		double mixNum = poitI[0].x+poitI[0].y;
		double maxNum = poitI[0].x+poitI[0].y; 
		double mixSub = poitI[0].x-poitI[0].y;
		double maxSub = poitI[0].x-poitI[0].y;
		for(int j = 0;j<poitII.length;j++)
		{
			double num = poitII[j].x+poitII[j].y;
			double sub = poitII[j].x-poitII[j].y;
			if(num > maxNum)
			{
				maxNum = num;
				poitI[2] = poitII[j];
			}
			if(num < mixNum)
			{
				mixNum = num;
				poitI[0] = poitII[j];
			}
			if(sub > maxSub)
			{
				poitI[3] = poitII[j];
				maxSub = sub;
			}
			if(sub < mixSub)
			{
				poitI[1] = poitII[j];
				mixSub = sub;
			}
		}
		double startX =  poitI[0].x;
		double startY = poitI[0].y;
		double endX = poitI[2].x;
		double endY = poitI[2].y;
		if(poitI[0].x < poitI[1].x)
		{
			startX = poitI[1].x;
		}
		if(poitI[2].x < poitI[3].x)
		{
			endX = poitI[2].x;
		}
		if(poitI[1].y < poitI[2].y)
		{
			endY = poitI[1].y;
		}
		if(poitI[0].y < poitI[3].y)
		{
			startY = poitI[3].y;
		}
		return new Rect(new Point(startX+pointD, startY+ pointD),new Point(endX-pointD,endY-pointD));
	}
	private Mat findImageTrue(Bitmap bitm)
	{ 
		Mat grayMat = null;
		Rect imageTureRect = null;
		MatOfPoint mContourtrue = null;
		//获取图片的宽和高
		int imageW = bitm.getWidth();
		int imageH = bitm.getHeight();
		//创建一个大小宽高和图片一样的Mat类
		 srcImageMat = new Mat(imageH, imageW, CvType.CV_8UC3);
		//将图片bitmap类转换成Mat
		 Utils.bitmapToMat(bitm, srcImageMat);
		//检测出图像边缘轮廓
		 findImageTrue_CannyMat = getCannyMat(srcImageMat);
		 
		 if(false)
		 {
			 displayMat(findImageTrue_CannyMat);
			 return null;
		 }
		 if(findImageTrue_CannyMat== null)
			 return null;
		//得到想要图形的轮廓的集合
		 mContourtrue = findContoursFromGrayMat(findImageTrue_CannyMat);
		 if(mContourtrue == null || mContourtrue.toList().size() != 4)
		 {
			 displayMat(findImageTrue_CannyMat);
			 return null;
		 }
//		 Scalar rgb = new Scalar(255,0,0);
		 imageTureRect = getImageTrueRect2(mContourtrue);//看不懂（应该是得到矩形的高度和宽度）
		 if(false)
		 {
			 displayMat(findImageTrue_CannyMat);
			 return null;
		 }
		 Scalar color = new Scalar( 255,0, 0);
		//最后要的矩形的轮廓的集合
		 List<MatOfPoint> matList = new ArrayList<MatOfPoint>();
		 matList.add(mContourtrue);
		//画线
	      Imgproc.drawContours( findImageTrue_CannyMat, matList, -1, color,1);
	      imageTureRect.width =srcImageMat.width();
	      imageTureRect.height = srcImageMat.height();
	      //imageTureRect.width = imageTureRect.width*2;
	      //imageTureRect.height = imageTureRect.height*2;
	     if(false)
	     {
	    	 displayMat(findImageTrue_CannyMat);
	    	 return null;
	     }
	      // Mat mat4 = new Mat(imageTureRect.height, imageTureRect.width, CvType.CV_8UC3);
	     // srcImageMat.copyTo(mat4);
	     dstImageMat = new Mat(imageTureRect.height, imageTureRect.width, CvType.CV_8UC3);
		//对图形进行切割
	     warpPersMat(srcImageMat,dstImageMat,mContourtrue);
	     
//	     if(true)
//	     {
//	    	 displayMat(dstImageMat);
//	    	 return null;
//	     }
		 //displayMat(dstImageMat);
		 return dstImageMat;
		// return null;
	}
	private void displayMat(Mat srcMat)
	{
		int imageW = bitmap.getWidth();
		int imageH = bitmap.getHeight();
		 Mat dstImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Imgproc.resize(srcMat, dstImage, dstImage.size());
		 //Imgproc.copyMakeBorder(srcMat, dstImage, 0, 0, srcMat.width(), srcMat.height(), Imgproc.BORDER_REPLICATE);
		Utils.matToBitmap(dstImage, bitmap);
	}
	private List<ImageInfo> CheckImage(Mat srcMat)
	{
		List<ImageInfo> emitList = new ArrayList<ImageInfo>();

		Mat grayMat2 = new Mat();
		//////////////////////////
 		grayMat2 = getTrueCannyMat(srcMat);

		if(false)
		{
			 displayMat(grayMat2);
			 return null;
		}	
 		
		if(grayMat2 == null)
			return null;
		Mat mat3 = grayMat2;
		//获取灰化后的图形轮廓
		List<MatOfPoint> mContourss = findEmitFromGrayMat(mat3);
		if(false)
		{
			 displayMat(grayMat2);
			 return null;
		}	
		
		int size = mContourss.size();
		for(int i = 0; i<size;i++)
		{
			ImageInfo imageInfo = new ImageInfo();
			////////////////////////////
			imageInfo = getShapeInfo(srcMat,mContourss.get(i));
			emitList.add(imageInfo);
			if(imageInfo!=null)
			{
			int color = imageInfo.color;
			Imgproc.drawContours( srcMat, mContourss, i, new Scalar(color&0x00ff0000,color&0x0000ff00,color&0x000000ff),1);
			}else
			{
				Imgproc.drawContours( srcMat, mContourss, i, new Scalar(0,0,0),1);
			}
		}
		displayMat(srcMat);
		return emitList;
	}
	private ImageInfo getShapeInfo(Mat strMat,MatOfPoint matPoint)
	{
		List<Point> pontList = matPoint.toList();
		/*if(pontList.size()> 15)
		{
			MatOfPoint contour = matPoint;
			MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );
			MatOfPoint2f approxCurve = new  MatOfPoint2f();
			Imgproc.approxPolyDP(new_mat, approxCurve, 2, true);
			matPoint = new MatOfPoint(approxCurve.toArray());
		}*/
		ImageInfo imageInfo = new ImageInfo();
		MatOfPoint2f matPoint2 = new MatOfPoint2f(matPoint.toArray());
		//获取图形的顶点个数
		pontList = matPoint.toList();
		if(pontList.size()<3)
			return null;
		int pointSize = pontList.size();
		//建立一个中心点坐标数组
		Point center = new Point();
		float[] radiu = new float[1];
		int[] radius = new int[pointSize];
		//获取图像外接圆
		Imgproc.minEnclosingCircle(matPoint2, center, radiu);
		//获取外接标准矩形
		Rect boundingRect = Imgproc.boundingRect(matPoint);
		///////////////////////////主要求得外接最小面积的矩形
		RotatedRect mainAregRect = Imgproc.minAreaRect(matPoint2);
		//获取一个2*2的小矩形作为重心
		int centerX = (int) (mainAregRect.center.x-2);
		int centerY = (int) (mainAregRect.center.y-2);
		MatOfInt matInt = new MatOfInt();
		//计算凸包的数量，轮廓上凸起的点，计算顶点
		Imgproc.convexHull(matPoint, matInt);
		MatOfPoint2f matPoint3 = new MatOfPoint2f();
		List<Integer> mathullList = matInt.toList();
		List<Point> matHullPointList = new ArrayList<Point>();
		
		for(int i = 0;i<mathullList.size();i++)
		{
			matHullPointList.add(pontList.get(mathullList.get(i)));
		}
		matPoint3.fromList(matHullPointList);
//		double mAir = Imgproc.contourArea(matPoint2);
		//封闭图形自身的面积
		double mHullAir = Imgproc.contourArea(matPoint3);
		//外接圆的面积
		double boundCircleAir =  3.14 *radiu[0]*radiu[0];
		//外接矩形的面积
		double boundRectAir = boundingRect.area();
		//这个函数决定这个点是否在轮廓内，外部，或者位于边缘(或者与顶点重合)。//外接圆的圆心
		double lstValue =  (int) Imgproc.pointPolygonTest(matPoint2, center, true);
		if(boundingRect.width>boundingRect.height)
			 boundRectAir =boundingRect.width*boundingRect.width;
		else
			boundRectAir =boundingRect.height*boundingRect.height;
		//int centerX = (int) (center.x-2);
		//int centerY = (int) (center.y-2);
		int boundTrctCenterX = boundingRect.x + boundingRect.width/2;
		int boundTrctCenterY = boundingRect.y + boundingRect.height/2;
		//Imgproc.pointPolygonTest(contour, pt, measureDist);
		int outBCount = matInt.toArray().length;
		int inBCount = matPoint.toArray().length - outBCount;
		//定义半径
		double R = 0;
		Point pointMCenter = new Point();
		Point pointRectCenter = mainAregRect.center;
		//
		Moments moment = Imgproc.moments(matPoint2);
		double m00 = moment.get_m00();
		double m01 = moment.get_m01();
		double m10 = moment.get_m10();
		double m11 = moment.get_m11();
		pointMCenter.x = (int) (m10/m00);
		pointMCenter.y = (int) (m01/m00);
		centerX = (int) (pointMCenter.x-2);
		centerY = (int) (pointMCenter.y-2);
		Scalar scalar = new Scalar(255);
		if(pointSize == 3 ||lstValue <=1)
		{
			scalar = getRectScaleFromeMat(strMat,new Rect(centerX,centerY,4,4));
			//imageInfo.pointCoint = 3;
			imageInfo.type = 1;				//三角形
		}
		else if(!check3Point(center, pointMCenter, pointRectCenter))
		{
			//imageInfo.pointCoint = 3;
			imageInfo.type = 1;				//三角形
//			if(outBCount>=5 &&inBCount>=5)
			if( (outBCount>=5 &&inBCount>=5) && (outBCount >=4&& outBCount<=6 ) )
			{
				//imageInfo.pointCoint = 5;
				imageInfo.type = 4;			//五角星
				Log.e("我这是五角星", "第一次五角星");
			}else if( (outBCount>=7 &&inBCount<=2))
			{
				//imageInfo.pointCoint = 5;
				imageInfo.type = 3;			//五角星
				Log.e("我这是五角星", "第一次五角星");
			}
			else
			{
				imageInfo.type = 1;
			}
			/*else if(mHullAir> boundCircleAir*2/3)
			{
				//imageInfo.pointCoint = 0;
				imageInfo.type = 3;
			}*/
		}
		
		else if(boundRectAir > boundCircleAir)
		{
			if(outBCount >=4&& outBCount<=6 && inBCount>=4 &&inBCount<=6)
			{
				
				//imageInfo.pointCoint = 5;
				imageInfo.type = 4;				//五角星
				Log.e("我这是五角星", "这是五角星");
			}
			else if(outBCount >=4&& inBCount<=4 &&outBCount<=5 && (inBCount+outBCount <10))
			//else if(outBCount >=6&& inBCount<=5 &&outBCount<=5 && (inBCount+outBCount <15))
			{
					//imageInfo.pointCoint = 4;
					imageInfo.type = 2;	

					Log.e("我这是矩形", "矩形");
			}
			else if(outBCount > 7 &&inBCount<outBCount/2 )
			{
				double w =  mainAregRect.size.width;
				double h =  mainAregRect.size.height;
				double mainRectS = w*h;
				if(w>h)
					mainRectS = w*w;
				else
					mainRectS = h*h;
				double scal = mHullAir / boundCircleAir;
//				if(mainRectS < boundCircleAir)
				if(inBCount<2)
				{
					imageInfo.type = 3;	
				}
				else if((mainRectS < boundCircleAir) /*&& (outBCount >=4&& inBCount<=4&&outBCount<=5 && (inBCount+outBCount <15))*/) 
				{
					imageInfo.type = 2;	
				}else
				//if((scal >= 0.99) && (scal < 1.05))
				{	
					//imageInfo.pointCoint = 4;
					imageInfo.type = 3;				//Բ��
				}
			}
//			else if(inBCount+outBCount > 10)
			else if((inBCount+outBCount > 10) ||/*&&*/ (outBCount >=4&& outBCount<=5 && inBCount>=4 &&inBCount<=6))
			{
				//imageInfo.pointCoint = 5;
				imageInfo.type = 4;				//�����
				Log.e("我这是五角星", "哈哈哈哈");
			}
			else
			{

				//imageInfo.pointCoint = 4;
				imageInfo.type = 2;				//����
				Log.e("我这是矩形", "矩形来了");
			}
		}
		else if(outBCount >=4&& outBCount<=6 && inBCount>=4 &&inBCount<=6)
		{
			//imageInfo.pointCoint = 5;
			imageInfo.type = 4;					//�����
			Log.e("我这是五角星", "嘿嘿嘿嘿嘿");
		}
//		else if(outBCount >= 7 &&inBCount<outBCount/2)
		else if(outBCount > 5 &&inBCount<outBCount/2)
		{
			//imageInfo.pointCoint = 0;
			imageInfo.type = 3;					//Բ��
		}else if(outBCount >=4 && outBCount <=6)
		{
			//imageInfo.pointCoint = 4;
			imageInfo.type = 2;					//����
			Log.e("我这是矩形", "矩形");
		}
		else
		{
			
			imageInfo = null;
			/*for(int i =0;i<pointSize;i++)
			{
				Point point = pontList.get(i);
				radius[i] = Math.abs((int) Imgproc.pointPolygonTest(mat2, point, true));
			}*/
			
		}
		if(imageInfo != null)//菱形的判断
		{	
			if(imageInfo.type == 2)
			{
				float bl1 = (float)boundingRect.width/(float)boundingRect.height;
				double mainAregRectS = mainAregRect.size.width*mainAregRect.size.height;
				float bl2 = (float)mHullAir/(float)mainAregRectS;
				if(bl2>0.8)
					imageInfo.type = 2;	
				else if(bl1>1.1||bl1<0.9)//矩形
					imageInfo.type = 5;	
			}
		}
		scalar = getRectScaleFromeMat(strMat,new Rect(centerX,centerY,4,4));
		int rgbR  = (int) scalar.val[0];
		int rgbG  = (int) scalar.val[1];
		int rgbB = (int) scalar.val[2];
		int rgbSum = (rgbR + rgbG + rgbB)/3;
		
		if(Math.abs(rgbR - rgbSum)<20 &&Math.abs(rgbG - rgbSum)<20&&Math.abs(rgbG - rgbSum)<20)
		{
			//小于20代表三种颜色接近
			//80是经验值，不需要大的改动
//			int R1  = rgbR > 80?255:0;
//			int G  = rgbR > 80?255:0;
//			int B =  rgbR > 80?255:0;
			
			int R1  = rgbR > 100?255:0;
			int G  = rgbR > 100?255:0;
			int B =  rgbR > 100?255:0;
			rgbR = R1;
			rgbG = G;
			rgbB = B;
		}
		else
		{	
			if(rgbR >= rgbSum)
				rgbR = 255;
			else
				rgbR = 0;
			if(rgbG >= rgbSum)
				rgbG = 255;
			else
				rgbG = 0;
			if(rgbB >= rgbSum)
				rgbB = 255;
			else
				rgbB = 0;
		}
		
		if(imageInfo != null)
		imageInfo.color = Color.argb(0, rgbR, rgbG, rgbB);
		Drawcenter(strMat,center,new Scalar(255,255,255));
		Drawcenter(strMat,pointMCenter,new Scalar(rgbR,rgbG,rgbB));
		Drawcenter(strMat,pointRectCenter,new Scalar(0,0,255));
		return imageInfo;
	}
	//两两判断坐标是否在在同一点
	boolean check2Point(Point point1 ,Point point2)
	{
		boolean ret = false;
		int xl = (int) Math.abs(point1.x - point2.x);
		int yl = (int) Math.abs(point1.y - point2.y);
		/*if(xl<3&&yl<6)
			ret =true;
		else if(xl<6&&yl<3)
			ret =true;
		else if(xl+yl < 7)
			ret =true;*/
		if(xl<3)
			ret =true;
		else if(yl<3)
			ret =true;
		return ret;
	}
	//判断重心，中心，外接圆的圆心是否在同一点
	boolean check3Point(Point point1 ,Point point2,Point point3)
	{
		boolean ret = false;
		boolean b1 = check2Point(point1,point2);
		boolean b2 = check2Point(point2,point3);
		boolean b3 = check2Point(point1,point3);
		if(b1 && b2 &&b3)
			ret = true;
		return ret;
	}
	void Drawcenter(Mat srcMat,Point center,Scalar scalar)
	{
		int centerX = (int) (center.x-2);
		int centerY = (int) (center.y-2);
		List<Point>pointList = new ArrayList<Point>();
		pointList.add(new Point(centerX,centerY));
		pointList.add(new Point(centerX,centerY+4));
		pointList.add(new Point(centerX+4,centerY+4));
		pointList.add(new Point(centerX+4,centerY));
		List<MatOfPoint> matList = new ArrayList<MatOfPoint>();
		MatOfPoint maoint = new MatOfPoint();
		maoint.fromList(pointList);
		matList.add(maoint);
		Imgproc.drawContours(srcMat, matList, -1, scalar);
	}
	private Scalar getRectScaleFromeMat(Mat src,Rect rect)
	{
		Scalar mBlobColorHsv = new Scalar(255);
		
		 Mat touchedRegionRgba;
		try {
			touchedRegionRgba = src.submat(rect);
		} catch (CvException e) {
			return null;
		}

	        Mat touchedRegionHsv = new Mat();
	        //Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

	        // Calculate average color of touched region
	        mBlobColorHsv = Core.sumElems(touchedRegionRgba);
	        int pointCount = rect.width*rect.height;
	        for (int i = 0; i < mBlobColorHsv.val.length; i++)
	            mBlobColorHsv.val[i] /= pointCount;
	        return mBlobColorHsv;
	}
	 private void warpPersMat(Mat src,Mat dst,MatOfPoint matPoint)
		{
			int resultWidth = dst.width();
			 int resultHeight = dst.height();
			//Mat startM = Converters.vector_Point2f_to_Mat(matPoint.toList());
			Point[] poitI = matPoint.toArray();
			Point[] poitII = matPoint.toArray();
			 
			
				double mixNum = poitI[0].x+poitI[0].y;
				double maxNum = poitI[0].x+poitI[0].y;
				double mixSub = poitI[0].x-poitI[0].y;
				double maxSub = poitI[0].x-poitI[0].y;
				for(int j = 0;j<poitII.length;j++)
				{
					double num = poitII[j].x+poitII[j].y;
					double sub = poitII[j].x-poitII[j].y;
					if(num >= maxNum)
					{
						maxNum = num;
						poitI[2] = poitII[j];
					}
					if(num <= mixNum)
					{
						mixNum = num;
						poitI[0] = poitII[j];
					}
					if(sub >= maxSub)
					{
						poitI[3] = poitII[j];
						maxSub = sub;
					}
					if(sub <= mixSub)
					{
						poitI[1] = poitII[j];
						mixSub = sub;
					}
				}
			
			 Point ocvPIn1 = new Point(poitI[0].x, poitI[0].y);
			 Point ocvPIn2= new Point(poitI[1].x, poitI[1].y);
			 Point ocvPIn3 = new Point(poitI[2].x, poitI[2].y);
			 Point ocvPIn4 = new Point(poitI[3].x, poitI[3].y);
			 List<Point> source = new ArrayList<Point>();
			 source.add(ocvPIn1);
			 source.add(ocvPIn2);
			 source.add(ocvPIn3);
			 source.add(ocvPIn4);
			 Mat startM = Converters.vector_Point2f_to_Mat(source);
			 Point ocvPOut1 = new Point(0, 0);
			 Point ocvPOut2 = new Point(0, resultHeight);
			 Point ocvPOut3 = new Point(resultWidth, resultHeight);
			 Point ocvPOut4 = new Point(resultWidth, 0);
			 List<Point> dest = new ArrayList<Point>();
			 dest.add(ocvPOut1);
			 dest.add(ocvPOut2);
			 dest.add(ocvPOut3);
			 dest.add(ocvPOut4);
			 Mat endM = Converters.vector_Point2f_to_Mat(dest);  
			 //Mat perspectiveTransform = new Mat(3, 3, CvType.CV_32FC1);
			 //Core.perspectiveTransform(startM, endM, perspectiveTransform);
			Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
			 Imgproc.warpPerspective(src, 
					 dst,
				       perspectiveTransform,
				       new Size(resultWidth, resultHeight), 
				       Imgproc.INTER_CUBIC);
		}
}
