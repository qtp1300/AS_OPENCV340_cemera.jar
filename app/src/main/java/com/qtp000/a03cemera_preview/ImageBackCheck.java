package com.qtp000.a03cemera_preview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageBackCheck {
	private Bitmap bitmap = null;
	private int backColor = Color.BLACK;
	private Mat imageMat = null;
	public Mat getImageMat() {
		return imageMat;
	}
	public ImageBackCheck(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
	}
	public void ImageBackRun()
	{
		Mat imageMat = findImageTrue(bitmap);
		if(imageMat == null)
			return;
		this.imageMat = imageMat;
		backColor = CheckImageBack(imageMat);
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public int getBackColor() {
		return backColor;
	}
	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}
	private Mat getCannyMat(Mat src)
	{
		double pointD = 1;
		double point2D =2;
		 Mat grayMat = new Mat();
		 Mat grayMat2 = new Mat();
		 Mat sholdMat = new Mat(); 
		 Mat cannyEdges = new Mat(); 
		 Mat equalMat = new Mat(); 
		 List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		 List<Point> pointList = null;
		 MatOfPoint matPoint = new MatOfPoint(new Point(pointD,pointD),new Point(pointD,src.size().height-pointD),new Point(src.size().width-point2D,src.size().height-point2D),new Point(src.size().width-point2D,point2D));
		 pointList = matPoint.toList();
		 contours.add(matPoint);
		 Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);
			
//		Imgproc.drawContours( grayMat, contours, -1, new Scalar(255,255,255),2);
		 Imgproc.Canny(grayMat, cannyEdges, 100, 20);
		 Imgproc.dilate(cannyEdges, grayMat2, new Mat());
         
         return grayMat2;
	}
	private Mat getTrueCannyMat(Mat src)
	{
		 Mat grayMat = new Mat();
		 Mat cannyEdges = new Mat(); 
		 Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);
		 Imgproc.Canny(grayMat, cannyEdges, 50, 5);
         return cannyEdges;
	}
	private MatOfPoint findContoursFromGrayMat(Mat grayMat)
	{
		double mMinContourArea = 0.1;
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		double maxAr =320*280*0.95;
		double maxArea = 0;
		 double mixArea = 0;
	        Iterator<MatOfPoint> each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint wrapper = each.next();
	            double area = Imgproc.contourArea(wrapper);
	            if (area > maxArea)
	                maxArea = area;
	            mixArea = maxArea;
	        }
	        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
	        each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint contour = each.next();
	            double area = Imgproc.contourArea(contour);
	            if ( area > mMinContourArea*maxArea &&area <maxAr) {
	                mContours.add(contour);
	            }
	        }
	        MatOfPoint2f approxCurve = new MatOfPoint2f();
	        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
	        each = mContours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint contour = each.next();
	            MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );
	            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
	            long total  = approxCurve.total();
	            if (total == 4 ) {
	            	 MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());
	            	mContour2.add(contour2);
	            }
	        }
	        Iterator<MatOfPoint> each2 = mContour2.iterator();
	        MatOfPoint mContour3 = null;
	        while (each2.hasNext()) {
	            MatOfPoint wrapper = each2.next();
	            double area = Imgproc.contourArea(wrapper);
	            List<Point> pointList = wrapper.toList();
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
		double mMinContourArea = 0.02;
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		double maxArea = 0;
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
		            if (area > mMinContourArea*maxArea ) 
		            {
		            	
		               
		                double[] m1 = hierarchy.get(0, i);
		                if(m1[2] == -1 && m1[3] != -1)
		                {
		                	 MatOfPoint2f approxCurve = new MatOfPoint2f();
		                 
		                	 MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );
		 	            	Imgproc.approxPolyDP(new_mat, approxCurve,3, true);
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
		int imageW = bitm.getWidth();
		int imageH = bitm.getHeight();
		 Mat srcImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Mat dstImage = null;
		 Utils.bitmapToMat(bitm, srcImage);

		//找出外轮廓
		 grayMat = getCannyMat(srcImage);
		 //displayMat(grayMat);
		 //if(true)
		 //return null;
		 Mat mat3 = new Mat(grayMat.height(), grayMat.width(), CvType.CV_8UC3);
		//就是将grayMat赋值给mat3
		 grayMat.copyTo(mat3);
		 if(mat3== null)
			 return null;
		 mContourtrue = findContoursFromGrayMat(mat3);
		 if(mContourtrue == null || mContourtrue.toList().size() != 4)
		 {
			 displayMat(grayMat);
			 return null;
		 }
			
		 Scalar rgb = new Scalar(255,0,0);
		 imageTureRect = getImageTrueRect2(mContourtrue);
		 Scalar color = new Scalar( 255,0, 0);
		 List<MatOfPoint> matList = new ArrayList<MatOfPoint>();
		 matList.add(mContourtrue);
//	      Imgproc.drawContours( grayMat, matList, -1, color,1);
		 //displayMat(grayMat);
		 //dstImage = srcImage.submat(imageTureRect);
	      Mat mat4 = new Mat(imageTureRect.height, imageTureRect.width, CvType.CV_8UC3);
		srcImage.copyTo(mat4);
		dstImage = new Mat(imageTureRect.height, imageTureRect.width, CvType.CV_8UC3);
		warpPersMat(mat4,dstImage,mContourtrue);
		 //displayMat(dstImage);
		 return dstImage;
		 //return null;
	}
	private void displayMat(Mat srcMat)
	{
		int imageW = bitmap.getWidth();
		int imageH = bitmap.getHeight();
		 Mat dstImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Imgproc.resize(srcMat, dstImage, dstImage.size());
		Utils.matToBitmap(dstImage, bitmap);
	}
	private int CheckImageBack(Mat src)
	{
		Scalar scalar = getRectScaleFromeMat(src,new Rect(src.width()/2,10,4,4));
		Scalar scalar1 = getRectScaleFromeMat(src,new Rect(src.width()/2,src.height()-10,4,4));
		if(scalar == null)
			return -1;
		// Mat Canny_grayMatE = new Mat(); 
		int rgbR  = (int) scalar.val[0];
		int rgbG  = (int) scalar.val[1];
		int rgbB = (int) scalar.val[2];
		int rgbSum = (rgbR + rgbG + rgbB)/3;//取出rgb的平均值
		int rgbMax = 0;
		if(rgbR > rgbMax)
			rgbMax = rgbR;
		if(rgbG > rgbMax)
			rgbMax = rgbG;
		if(rgbB > rgbMax)
			rgbMax = rgbB;
		double light = 1;
		int alp = 50;
		if(rgbMax < 100)//对比度调高
		{
			light = 1;
			alp = 80;
		}
		else if(rgbMax < 170)
		{
			light = 1;				//当前光照强度
			alp = 50;				//当前对比度
		}
		else//�Աȶȵ���
		{
			light = 0.9;
			alp = 40;
		}
		if(Math.abs(rgbR - rgbSum)<20 &&Math.abs(rgbG - rgbSum)<20&&Math.abs(rgbG - rgbSum)<20)
		{
			//小于20代表三种颜色接近
			//80是经验值，不需要大的改动
			int R  = rgbR > 80?255:0;
			int G  = rgbR > 80?255:0;
			int B =  rgbR > 80?255:0;
			rgbR = R;
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
		int color = Color.argb(0, rgbR, rgbG, rgbB);
		Drawcenter(src,new Point(src.width()/2-2,10-2),new Scalar(255,0,0));
		if(scalar1 == null)
			return -1;
		// Mat Canny_grayMatE = new Mat(); 
		int rgbR1  = (int) scalar1.val[0];
		int rgbG1  = (int) scalar1.val[1];
		int rgbB1 = (int) scalar1.val[2];
		int rgbSum1 = (rgbR1 + rgbG1 + rgbB1)/3;//取出rgb的平均值
		int rgbMax1 = 0;
		if(rgbR1 > rgbMax1)
			rgbMax1 = rgbR1;
		if(rgbG1 > rgbMax1)
			rgbMax1 = rgbG1;
		if(rgbB1 > rgbMax1)
			rgbMax1 = rgbB1;
		double light1 = 1;
		int alp1 = 50;
		if(rgbMax1 < 100)//对比度调高
		{
			light1 = 1;
			alp1 = 80;
		}
		else if(rgbMax1 < 170)
		{
			light1 = 1;				//当前光照强度
			alp1 = 50;				//当前对比度
		}
		else//对比度调低
		{
			light1 = 0.9;
			alp1 = 40;
		}
		if(Math.abs(rgbR1 - rgbSum1)<20 &&Math.abs(rgbG1 - rgbSum1)<20&&Math.abs(rgbG1 - rgbSum1)<20)
		{
			//小于20代表三种颜色接近
			//80是经验值，不需要大的改动
			int R  = rgbR1 > 80?255:0;
			int G  = rgbR1 > 80?255:0;
			int B =  rgbR1 > 80?255:0;
			rgbR1 = R;
			rgbG1 = G;
			rgbB1 = B;
		}
		else
		{
			if(rgbR1 >= rgbSum1)
				rgbR1 = 255;
			else
				rgbR1 = 0;
			if(rgbG1 >= rgbSum1)
				rgbG1 = 255;
			else
				rgbG1 = 0;
			if(rgbB1 >= rgbSum1)
				rgbB1 = 255;
			else
				rgbB1 = 0;
		}
		int color1 = Color.argb(0, rgbR1, rgbG1, rgbB1);
		Drawcenter(src,new Point(src.width()/2-2,src.height()-10+2),new Scalar(255,0,0));
		displayMat(src);
		if(color1 != color)
			return -1;
		return color;
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
			int resultWidth = src.width();
			 int resultHeight = src.height();
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
			Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
			 Imgproc.warpPerspective(src, 
					 dst,
				       perspectiveTransform,
				       new Size(resultWidth, resultHeight), 
				       Imgproc.INTER_CUBIC);
		}
}
