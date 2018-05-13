package com.qtp000.a03cemera_preview.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
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

import com.qtp000.a03cemera_preview.EmitContur;
import com.qtp000.a03cemera_preview.Image.ColorBlobDetector;

public class ImageBack {
	private Bitmap bitmap = null;
	private int backColor = Color.BLACK;
	private List<EmitContur> emitConturList =  new ArrayList<EmitContur>();
    private ColorBlobDetector mDetector;
	public List<EmitContur> getEmitConturList() {
		return emitConturList;
	}
	public ImageBack(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
	}
	public void ImageBackRun()
	{
		Mat imageMat = findImageTrue(bitmap);
		if(imageMat == null)
			return;
		emitConturList = CheckImage(imageMat);
	}
	public void ImageBackRun(Mat imageMat)
	{
		if(imageMat == null)
			return;
		emitConturList = CheckImage(imageMat);
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
	private Mat getCannyMat(Mat src)        //灰化后检测边缘然后对边缘进行膨胀
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
			/*找到面积大于总像素十分之一的轮廓，找到其中四边形，找到宽高比例在1~2之间的四边形，
			且选择面积最小的轮廓（根据前几步处理结果，显示屏外轮廓会是最小轮廓）*/
	{
		double mMinContourArea = 0.1;       //最小轮廓区域
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object
		Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		//学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
		//hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
		double maxAr =320*280*0.95;
		double maxArea = 0;
		 double mixArea = 0;
	        Iterator<MatOfPoint> each = contours.iterator();        //1迭代器，依次取出contours内的各个轮廓
	        while (each.hasNext()) {                                //3遍历轮廓集合，得到最大轮廓面积maxArea
	            MatOfPoint wrapper = each.next();
	            double area = Imgproc.contourArea(wrapper);         //2轮廓List中点的面积
	            if (area > maxArea)
	                maxArea = area;
	            mixArea = maxArea;
	        }
	        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
	        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
	        each = contours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint contour = each.next();
	            double area = Imgproc.contourArea(contour);
	            if ( area > mMinContourArea*maxArea &&area <maxAr) {
	                mContours.add(contour);
	            }
	        }
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/


	        MatOfPoint2f approxCurve = new MatOfPoint2f();
	        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
	        each = mContours.iterator();
	        while (each.hasNext()) {
	            MatOfPoint contour = each.next();
	            MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );       //把轮廓的点转化为数组并以数组建立新的MatOfPoint对象new_mat
	            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
	            //Imgproc.approxPolyDP(输入的轮廓点的点集，输出的多边形点集，输出精度——和另一个轮廓点的最大距离数，输出的多边形是否闭合)
	            long total  = approxCurve.total();      //边的数量？
	            if (total == 4 ) {
	            	 MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());       //把边的点集转化为MatOfPoint
	            	mContour2.add(contour2);        //把点集的MatOfPoint加入列表mContour2
	            }
	        }


	        Iterator<MatOfPoint> each2 = mContour2.iterator();      //把轮廓的集进行迭代
	        MatOfPoint mContour3 = null;
	        while (each2.hasNext()) {
	            MatOfPoint wrapper = each2.next();
	            double area = Imgproc.contourArea(wrapper);     //计算轮廓面积
	            List<Point> pointList = wrapper.toList();       //把轮廓转化为List
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
	private Mat findImageTrue(Bitmap bitm)      //
	{ 
		Mat grayMat = null;
		Rect imageTureRect = null;
		MatOfPoint mContourtrue = null; 
		int imageW = bitm.getWidth();
		int imageH = bitm.getHeight();
		 Mat srcImage = new Mat(imageH, imageW, CvType.CV_8UC3);
		 Mat dstImage = null;
		 Utils.bitmapToMat(bitm, srcImage);     //转化为Mat
		 
		//�ҳ�������
		 grayMat = getCannyMat(srcImage);       //grayMat是灰化后边缘检测的结果膨胀处理
		 //displayMat(grayMat);
		 //if(true)
		 //return null;
		 Mat mat3 = new Mat(grayMat.height(), grayMat.width(), CvType.CV_8UC3);
		 //���ǽ�grayMat��ֵ��mat3
		 grayMat.copyTo(mat3);              //将grayMat复制到mat3
		 if(mat3== null)
			 return null;
		 mContourtrue = findContoursFromGrayMat(mat3);      //返回了一个Matofpoint类型
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
	private List<EmitContur> CheckImage(Mat srcMat)
	{
		List<EmitContur> emitList = new ArrayList<EmitContur>();
		Mat grayMat = new Mat();
		Mat grayMat2 = new Mat();
		Mat grayMat3 = new Mat();
		Mat alpMat = new Mat();
		Imgproc.blur(srcMat, grayMat2, new Size(3,3));
		
		grayMat = getTrueCannyMat(grayMat2);
		Imgproc.blur(grayMat, grayMat2, new Size(3,3));
		if(grayMat2 == null)
			return null;
		if(false)
		{
			displayMat(grayMat2);
			return null;
		}
		Mat mat3 = grayMat2;
		List<MatOfPoint> mContourss = findEmitFromGrayMat(mat3);
		int size = mContourss.size();
		for(int i = 0; i<size;i++)
		{
			MatOfPoint ofPoint = mContourss.get(i);
			List<Point> poitList = ofPoint.toList();
			double row = poitList.get(0).y;
			double col = poitList.get(0).x;
			Scalar mBlobColorHsv = new Scalar(srcMat.get((int)(row), (int)(col)));
			int R  = mBlobColorHsv.val[0] > 80?255:0;
			int G  = mBlobColorHsv.val[1] > 80?255:0;
			int B = mBlobColorHsv.val[2] > 80?255:0;
			Scalar rgb = new Scalar(R,G,B);
//			Imgproc.drawContours( srcMat, mContourss, i, rgb,1);
			
			EmitContur emitContur = new EmitContur(poitList, Color.argb(0,R, G, B));
			emitList.add(emitContur);
		}
		displayMat(srcMat);
		return emitList;
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
