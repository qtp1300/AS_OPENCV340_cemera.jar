package com.qtp000.a03cemera_preview.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
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

import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;

public class TrafficImage {
	private Bitmap bitmap = null;
	private Bitmap bitmapOld = null;
	private int backColor = Color.BLACK;
	private List<Integer> emitConturList =  new ArrayList<Integer>();
	public List<Integer> getEmitConturList() {
		return emitConturList;
	}
	public TrafficImage(Bitmap bitmap,Bitmap bitmap2) {
		super();
		this.bitmap = bitmap;
		this.bitmapOld = bitmap2;
		//BimapFindContours();
		//Mat imageMat = findImageTrue(bitmap);
		Mat imageMat = findImageTrue(bitmap);
		if(imageMat == null)
			return;
		emitConturList = CheckImage(imageMat);
		//displayMat(imageMat);
	}
	public TrafficImage(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
		//BimapFindContours();
		
	}
	public void TrafficImageRun()
	{
		Mat imageMat = findImageTrue(bitmap);
		if(imageMat == null)
			return;
		emitConturList = CheckImage(imageMat);
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
			
		 /*Imgproc.dilate(grayMat, grayMat2, new Mat());
		 Imgproc.equalizeHist(grayMat2, equalMat);*/
		// Imgproc.GaussianBlur(grayMat, grayMat2, new Size(3,3), Imgproc.BORDER_DEFAULT);
        // Imgproc.Canny(grayMat, cannyEdges, 100, 500);
		// Imgproc.Canny(grayMat, cannyEdges, 100, 500);
         //Imgproc.adaptiveThreshold(grayMat, sholdMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 101, 10);
       //Imgproc.dilate(sholdMat, grayMat2, new Mat(),new Point(-1,-1),1);
			Imgproc.drawContours( grayMat, contours, -1, new Scalar(255,255,255),2);
			Imgproc.Canny(grayMat, cannyEdges, 100, 100);
			Imgproc.dilate(cannyEdges, grayMat2, new Mat());
         //Imgproc.HoughLinesP()
         
         return grayMat2;
         //return cannyEdges;
	}
	private Mat getTrueCannyMat(Mat src)
	{
		 Mat grayMat = new Mat();
		 Mat cannyEdges = new Mat(); 
		 Mat grayMat2 = new Mat();
		 Mat equalMat = new Mat();
		 Mat sholdMat = new Mat();
		 int width = src.width();
		 int height = src.height();
		 int cX = width/2;
		 int cY = height/2;
		 int linWith = width*2/3;
		 int linHeight = height*2/3;
		 double pointD = 20;
			double point2D =pointD*2;
		 //Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);
		 //Imgproc.dilate(grayMat, grayMat2, new Mat());
        // Imgproc.Canny(grayMat, cannyEdges, 100, 100);
         //return cannyEdges;
		
		 //Imgproc.Canny(grayMat, cannyEdges, 10, 100);
        // Imgproc.Canny(src, cannyEdges, 30, 100);
		 //Imgproc.dilate(grayMat, cannyEdges, new Mat());
		// Imgproc.blur(grayMat, cannyEdges, new Size(3,3));
		 //Imgproc.GaussianBlur(grayMat, grayMat2, new Size(3,3), Imgproc.BORDER_DEFAULT);
		// Imgproc.adaptiveThreshold(grayMat, sholdMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 3, 2);
		// Imgproc.equalizeHist(sholdMat, equalMat);
         //Imgproc.dilate(grayMat2, cannyEdges, new Mat());
		// List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		 //List<Point> pointList = null;
			// MatOfPoint matPoint = new MatOfPoint(new Point(pointD,pointD),new Point(pointD,src.size().height-pointD),new Point(src.size().width-point2D,src.size().height-point2D),new Point(src.size().width-point2D,point2D));
			/// pointList = matPoint.toList();
		//contours.add(matPoint);
		//Imgproc.drawContours( src, contours, -1, new Scalar(255,255,255),2);
		//Core.line(src, new Point(cX-linWith/2,0), new Point(cX+linWith/2,0), new Scalar(255,255,255),1);
		//Core.line(src, new Point(cX-linWith/2,height-1), new Point(cX+linWith/2,height-1), new Scalar(255,255,255),1);
		//Core.line(src, new Point(0,cY-linHeight/2), new Point(0,cY+linHeight/2), new Scalar(255,255,255),2);
		//Core.line(src, new Point(width-1,cY-linHeight/2), new Point(width-1,cY+linHeight/2), new Scalar(255,255,255),1);
		Imgproc.Canny(src, cannyEdges, 100, 300);
         return cannyEdges;
	}
	private MatOfPoint findContoursFromGrayMat(Mat grayMat)
	{
		double mMinContourArea = 0.05;
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		
		double maxArea = 320*280;
		 double mixArea = 0;
	        Iterator<MatOfPoint> each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint wrapper = each.next();
	            double area = Imgproc.contourArea(wrapper);
	            if (area > maxArea )
	                maxArea = area;
	            mixArea = maxArea;
	        }
	        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
	        each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint contour = each.next();
	            double area = Imgproc.contourArea(contour);
	            if ( area > mMinContourArea*maxArea ) {
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
	          
	            if( b1 > 1 &&b1 < 2.5)
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
		  double imageWidth = grayMat.width();
          double imageHeight = grayMat.height();
		double mMinContourArea = 0.1;
		double mMinArea =imageWidth*imageHeight*mMinContourArea;
		double mMaxArea =imageWidth*imageHeight*0.9;
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		double maxArea = 0;
		int maxAindex = 0;
	
		List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
	        int size = contours.size();
	        for(int i = 0;i< size;i++)
	        {
	        	MatOfPoint contour = contours.get(i);
	         double area = Imgproc.contourArea(contour);
		            if (area > maxArea && area<mMaxArea) 
		            {
		            	maxArea = area;
		            	maxAindex = i;
		               
		                
		            }
	        }
	        if(size > 0)
	        {
	        	MatOfPoint contour = contours.get(maxAindex);
	        	 MatOfPoint2f approxCurve = new MatOfPoint2f();
               
            	 MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray());
	            	Imgproc.approxPolyDP(new_mat, approxCurve,10, true);
	            	MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());
	            	maxArea = Imgproc.contourArea(contour);
	            	if(maxArea >mMinArea)
	            	mContours.add(contour2);
	        }
		return mContours;
	}
	private Rect getImageTrueRect3(MatOfPoint matPoint)
	{
		double pointD = 1;
		List<Point> pointList = matPoint.toList();
		 double startX = 0;
		    double startY = 0;
		    double endX = 0;
		    double endY = 0;
		    if(pointList.get(0).x > pointList.get(3).x)
		    startX = pointList.get(3).x;
		    else
		    startX = pointList.get(0).x;
		    if(pointList.get(0).y > pointList.get(1).y)
			    startY = pointList.get(1).y;
			    else
			    startY = pointList.get(0).y;
		    if(pointList.get(2).x > pointList.get(1).x)
			    endX = pointList.get(1).x;
			    else
			    endX = pointList.get(2).x;
			 if(pointList.get(3).y > pointList.get(2).y)
				    endY = pointList.get(2).y;
				    else
				    endY = pointList.get(3).y;
		    
		    return new Rect(new Point(startX+pointD, startY+ pointD),new Point(endX-pointD,endY-pointD));
	}
	private Rect getImageTrueRect2(MatOfPoint matPoint)
	{
		double pointD = 1;
		List<Point> pointList = matPoint.toList();
		 double startX = 0;
		    double startY = 0;
		    double endX = 0;
		    double endY = 0;
		    if(pointList.get(0).x > pointList.get(3).x)
		    startX = pointList.get(3).x;
		    else
		    startX = pointList.get(0).x;
		    if(pointList.get(0).y > pointList.get(1).y)
			    startY = pointList.get(1).y;
			    else
			    startY = pointList.get(0).y;
		    if(pointList.get(2).x > pointList.get(1).x)
			    endX = pointList.get(1).x;
			    else
			    endX = pointList.get(2).x;
			 if(pointList.get(3).y > pointList.get(2).y)
				    endY = pointList.get(2).y;
				    else
				    endY = pointList.get(3).y;
		    
		    return new Rect(new Point(startX+pointD, startY+ pointD),new Point(endX-pointD,endY-pointD));
	}
	private Rect getImageTrueRect(MatOfPoint matPoint)
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
		Mat alpMat = new  Mat();
		Rect imageTureRect = null;
		MatOfPoint mContourtrue = null; 
		int imageW = bitm.getWidth();
		int imageH = bitm.getHeight();
		 Mat srcImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Mat dstImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Utils.bitmapToMat(bitm, srcImage);
		 srcImage.convertTo(alpMat, -1,1.5,60);
		 
		 
		// grayMat = getCannyMat(srcImage);
		 grayMat = getCannyMat(alpMat);
		  Mat grayMat2 = grayMat;
		  //displayMat(grayMat);
		 //if(grayMat== null||grayMat!= null)
		  if(grayMat== null)
			 return null;
		  //if(true)
			//return null;
		 mContourtrue = findContoursFromGrayMat(grayMat2);
		 if(mContourtrue == null || mContourtrue.toList().size() != 4)
		 {
			 displayMat(grayMat);
			 return null;
		 }
			
		 Scalar rgb = new Scalar(255,0,0);
		 //imageTureRect = getImageTrueRect(mContourtrue);
		 //imageTureRect=Imgproc.boundingRect(mContourtrue);
		// Scalar color = new Scalar( 255,0, 0);
		// List<MatOfPoint> matList = new ArrayList<MatOfPoint>();
		// matList.add(mContourtrue);
	      //Imgproc.drawContours( grayMat, matList, -1, color,1);
		 //displayMat(grayMat);
		 Mat mat3 = new Mat(srcImage.height(), srcImage.width(), CvType.CV_8UC3);
		 srcImage.copyTo(mat3);
		 warpPersMat(mat3,dstImage,mContourtrue);
		 //dstImage = srcImage.submat(imageTureRect);
		//displayMat(dstImage);
		// return null;
		 return dstImage;
	}
	private void warpPersMat(Mat src,Mat dst,MatOfPoint matPoint)
	{
		int resultWidth = src.width();
		 int resultHeight = src.height();
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
	private void displayMat(Mat srcMat)
	{
		int imageW = bitmap.getWidth();
		int imageH = bitmap.getHeight();
		 Mat dstImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Imgproc.resize(srcMat, dstImage, dstImage.size());
		Utils.matToBitmap(dstImage, bitmap);
	}
	private List<Integer> CheckImage(Mat srcImage)
	{
		List<Integer> emitList = new ArrayList<Integer>();
		int srcWidth = srcImage.width();
		int srcHeight =  srcImage.height();
		if(srcWidth < 10 ||srcHeight< 10)
			return emitList;
		Mat grayMat = new Mat();
		Mat grayMat2 = new Mat();
		Mat grayMat3 = new Mat();
		Mat alpMat = new Mat();
		//Mat srcMat = new Mat(srcMat.height()-4, srcMat.width()-4, CvType.CV_8UC3);
		Mat srcMat = srcImage.submat(2,srcHeight-2,2,srcWidth-2);
		Imgproc.dilate(srcMat, grayMat, new Mat(),new Point(-1,-1),5);
		//grayMat2.convertTo(alpMat, -1,1.5,50);
		grayMat2 = getTrueCannyMat(grayMat);
		Imgproc.dilate(grayMat2, grayMat3, new Mat(),new Point(-1,-1),2);
		if(grayMat == null)
			return null;
		//Imgproc.dilate(grayMat, grayMat2, new Mat(),new Point(-1,-1),2);
		Mat mat3 = new Mat(grayMat3.height(), grayMat3.width(), CvType.CV_8UC3);
		grayMat3.copyTo(mat3);
		//displayMat(grayMat3);
		//if(true)
		//return null;
		List<MatOfPoint> mContourss = findEmitFromGrayMat(mat3);
		int size = mContourss.size();
		if(size != 1)
		{
			displayMat(grayMat3);
			return null;
		}
		if(size == 1)
		{
			MatOfPoint ofPoint = mContourss.get(0);
			List<Point> pointList1 = new ArrayList<Point>();
			List<Point> pointList2 = new ArrayList<Point>();
			Rect rect = Imgproc.boundingRect(ofPoint);
			int with = rect.width;
			int height = rect.height;
			int cX = with/2 + rect.x;
			int cY = height/2 + rect.y;
			List<Point> poitList = ofPoint.toList();
			for(int i = 0;i <poitList.size();i++)
			{
				Point point = poitList.get(i);
				if(point.x<=cX)
				{
					pointList1.add(point);
					pointList2.add(new Point(cX+1,point.y));
				}
				else
				{
					pointList2.add(point);
					pointList1.add(new Point(cX-1,point.y));
				}
			}
			MatOfPoint ofPoint1 = new MatOfPoint();
			ofPoint1.fromList(pointList1);
			MatOfPoint ofPoint2 = new MatOfPoint();
			ofPoint2.fromList(pointList2);
			
			List<MatOfPoint> curslist1 = new ArrayList<MatOfPoint>();
			curslist1.add(ofPoint1);
			List<MatOfPoint> curslist2= new ArrayList<MatOfPoint>();
			curslist2.add(ofPoint2);
			double area1 = Imgproc.contourArea(ofPoint1);
			double area2 = Imgproc.contourArea(ofPoint2);
			
			
			float farea = (float)area1/(float)area2;
			int dreation =-1;
			int color = Color.argb(0,0, 0, 0);
			int R =0,G=0,B=0;
			if(farea>0.8 &&farea <1.2)
			{
				Scalar scalar = getRectScaleFromeMat(srcMat,new Rect(cX-5,rect.y +2,10,10));
				int rgbR  = (int) scalar.val[0];
				int rgbG  = (int) scalar.val[1];
				int rgbB = (int) scalar.val[2];
				int rgbSum = (rgbR + rgbG + rgbB)/3;
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
				if(rgbR == 255&&rgbG == 255)
					rgbR = 0;
				color = Color.argb(0,rgbR, rgbG, rgbB);
				/*R  = (int) (scalar.val[0] > 250?0:scalar.val[0]);
				G  = (int) (scalar.val[1] > 250?0:scalar.val[1]);
				B = 0;
				if(R>G)
				{
					R = 255;
					G = 0;
				}else
				{
					R = 0;
					G = 255;
				}
				color = Color.argb(0,R, G, B);*/
				dreation = 2;
				R= rgbR;
				G = rgbG;
				B = rgbB;
				emitList.add(dreation);
				emitList.add(color);
			}else
			{
				Scalar scalar1 = getRectScaleFromeMat(srcMat,new Rect(cX-5,cY-5,10,10));
				Scalar scalar2 = getRectScaleFromeMat(srcMat,new Rect(cX+10,cY+10,10,10));
				/*R  = (int) (scalar1.val[0] > 250?0:scalar1.val[0]);
				G  = (int) (scalar1.val[1] > 250?0:scalar1.val[1]);
				B = 0;
				int R1  = (int) (scalar2.val[0] > 250?0:scalar2.val[0]);
				int G1  = (int) (scalar2.val[1] > 250?0:scalar2.val[1]);
				int B1 = 0;
				if(R>G)
				{
					R = 255;
					G = 0;
				}else
				{
					R = 0;
					G = 255;
				}
				if(R1>G1)
				{
					R1 = 255;
					G1 = 0;
				}else
				{
					R1 = 0;
					G1 = 255;
				}
				if(R==R1&&G == G1)
				{
					color = Color.argb(0,R, G, B);
				}else
				{
					color = Color.argb(0,0, 0, 0);
				}*/
				int rgbR  = (int) scalar1.val[0];
				int rgbG  = (int) scalar1.val[1];
				int rgbB = (int) scalar1.val[2];
				int rgbSum = (rgbR + rgbG + rgbB)/3;
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
				if(rgbR == 255&&rgbG == 255)
					rgbR = 0;
				color = Color.argb(0,rgbR, rgbG, rgbB);
				if(farea<0.8 && farea>0.2)
				{
					dreation = 1;
				}else if(farea<1.8 && farea>1.2)
				{
					dreation = 0;
				}else
				{
					dreation = -1;
				}
				emitList.add(dreation);
				emitList.add(color);
				R= rgbR;
				G = rgbG;
				B = rgbB;
			}
			Imgproc.drawContours( srcMat, curslist1, -1, new Scalar(R, G, B),1);
			Imgproc.drawContours( srcMat, curslist2, -1, new Scalar(R, G, B),1);
		}
		/*for(int i = 0; i<size;i++)
		{
			MatOfPoint ofPoint = mContourss.get(i);
			List<Point> poitList = ofPoint.toList();
			double row = poitList.get(0).y;
			double col = poitList.get(0).x;
			Scalar mBlobColorHsv = new Scalar(grayMat2.get((int)(row), (int)(col)));
			Scalar mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);
			int R  = mBlobColorHsv.val[0] > 80?255:0;
			int G  = mBlobColorHsv.val[1] > 80?255:0;
			int B = mBlobColorHsv.val[2] > 80?255:0;
			//Scalar rgb = new Scalar(R,G,B);
			Scalar rgb = new Scalar(255,255,255);
			Imgproc.drawContours( srcMat, mContourss, i, rgb,1);
			
			EmitContur emitContur = new EmitContur(poitList, Color.argb(0,R, G, B));
			emitList.add(emitContur);
		}*/
		displayMat(srcMat);
		//displayMat(alpMat);
		return emitList;
	}
	private Scalar getRectScaleFromeMat(Mat src,Rect rect)
	{
		Scalar mBlobColorHsv = new Scalar(255);
		 Mat touchedRegionRgba = src.submat(rect);

	        Mat touchedRegionHsv = new Mat();
	       // Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

	        // Calculate average color of touched region
	        mBlobColorHsv = Core.sumElems(touchedRegionRgba);
	        int pointCount = rect.width*rect.height;
	        for (int i = 0; i < mBlobColorHsv.val.length; i++)
	            mBlobColorHsv.val[i] /= pointCount;
	        return mBlobColorHsv;
	}
	 private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
	        Mat pointMatRgba = new Mat();
	        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
	        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

	        return new Scalar(pointMatRgba.get(0, 0));
	    }
}
