//package com.qtp000.a03cemera_preview.Image;
//
//public class Temp_Study {
//    public static Bitmap shapebitmap = null;
//    private Bitmap shapebit = MainActivity.bitmap;
//    public static String shaperesult = "";
//    //颜色
//    public String[] color = new String[4];
//    //形状
//    public String[] shape = new String[5];
//    //个数
//    public int num[][] = new int[8][5];//第一个数代表颜色，第二个代表形状
//    public void checkShecp()
//    {
//        shaperesult = "";
//        shapebitmap = MainActivity.bitmap;
//        new FileService().savePhoto(shapebitmap ,Result_Name.Picture_Name + ".png");
//        if(shapebitmap == null)
//            return;
//        ImageShapeBack imageShapeBack = null;
//        Bitmap bitma = null;
//        //bitmap  = Bitmap.createBitmap(newbitmap);
//        //从摄像头那里获得图片
//        shapebit  = Bitmap.createBitmap(shapebitmap);       //获取到图片
//        imageShapeBack = new ImageShapeBack(shapebit);      //构造函数，把图片传进去
//        //图片处理
//        if(currectMat == null)
//            imageShapeBack.ImageBackRun();
//        else
//            imageShapeBack.ImageBackRun(currectMat);
//        //获取处理后的图片
//        shapebitmap = imageShapeBack.getBitmap();
//        List<ImageInfo> emitList = imageShapeBack.getImageInfoList();
//        if(emitList == null ||emitList.size() == 0)
//        {
////			if(bitma!= null)
////			{	picflag = 1;
////				showView.setImageBitmap(bitma);
////			}
////			result.setText("error");
//            return;
//        }
//        //颜色
//        String[] colorString = {"红色","绿色","蓝色","黄色","品色","青色","黑色","白色"};
//        //形状
//        String[] shapString = {"三角形","矩形","圆形","五角星","菱形"};
//        //个数
//        int emitArry[][]  = new int[8][5];
//
//        for(int i = 0;i<emitList.size();i++)
//        {
//            ImageInfo imageInfo = emitList.get(i);
//            if(imageInfo == null)
//                continue;
//            int type = imageInfo.type;
//            int color = imageInfo.color;
//            for(int j = 0; j < 5;j++)
//            {
//                int ty = type-1;
//                if(ty==j && color ==0xFF0000)//红色
//                    emitArry[0][ty]++;
//                else if(ty==j && color ==0x00FF00)//绿色
//                    emitArry[1][ty]++;
//                else if(ty==j && color ==0x0000FF)//蓝色
//                    emitArry[2][ty]++;
//                else if(ty==j && color ==0xFFFF00)//黄色
//                    emitArry[3][ty]++;
//                else if(ty==j && color ==0xFF00FF)//品色
//                    emitArry[4][ty]++;
//                else if(ty==j && color ==0x00FFFF)//青色
//                    emitArry[5][ty]++;
//                else if(ty==j && color ==0x000000)//黑色
//                    emitArry[6][ty]++;
//                else if(ty==j && color ==0xFFFFFF)//白色
//                    emitArry[7][ty]++;
//            }
//        }
//        for(int i = 0;i<8;i++)
//        {
//            for(int j = 0;j<5;j++)
//            {
//                shaperesult += colorString[i] + shapString[j] + emitArry[i][j] + "个;";
//
//                try{
////					color[i] = colorString[i];
////	//				Log.e("识别到的颜色：" + colorString[i], "要操作的颜色：" + color[i]);
////	//
////					shape[j] = shapString[j];
////	//				Log.e("识别到的形状：" + shapString[j], "要操作的形状：" + shape[j]);
////	//
//                    num[i][j] = emitArry[i][j];
//                    //				Log.e("识别到的个数：" + emitArry[i][j], "要操作的个数：" + num[i][j]);
//                }
//                catch(Exception e)
//                {
//                    return;
//                }
////				Log.e("**********************", "********************************");
//            }
//            shaperesult +="\n";
//        }
//    }
//}
//
//
//    ///********************************************************************************************************
/////////////////////////////////////图形图像识别
//    public static Bitmap backbitmap = null;
//    private Bitmap backbit = MainActivity.bitmap;
//
//    public int checkBack()//检测背景颜色
//    {
//        shaperesult = "";
//        backbitmap = MainActivity.bitmap;
//        new FileService().savePhoto(backbitmap ,Result_Name.Picture_Name + ".png");
//        if(backbitmap == null)
//            return -1;
//        ImageBackCheck imageBackCheck = null;
//        Bitmap bitma = null;
//        //bitmap  = Bitmap.createBitmap(newbitmap);
//        //从摄像头那里获得图片
//        backbit  = Bitmap.createBitmap(backbitmap);
//        imageBackCheck = new ImageBackCheck(backbit);
//        //图片处理
//        imageBackCheck.ImageBackRun();
//        //shapebitmap = imageBackCheck.getBitmap();
//        //获取处理后的图片
//        backbit = imageBackCheck.getBitmap();
//        int color = imageBackCheck.getBackColor();
//
//        currectMat = imageBackCheck.getImageMat();
//
//        return 	color;
//    }
//}
