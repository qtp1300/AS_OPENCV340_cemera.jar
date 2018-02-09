package com.qtp000.a03cemera_preview;

/**
 * Created by 祁天培 on 2018/2/9.
 */

public class Algorithm {
//	public static void main(String[] args)
//	{
//		CRC_Code("(<AaBbCcDd>/<g(x)=x16+x15+x2+1>)",CRC_order_data);
//
//		Affine("A/5,2;B\\C[D.y(EF]Gf,3,7",Affine_order_data);
//
//		RSA_Code("(<3,4,5,7,8,9,19>/<1,2,3,4,5,6>)",RSA_order_data);
//	}


    /****************************************************************************************************
     RSA
     ****************************************************************************************************/

    //RSA解密
    public  void RSA_Code(String SrcString,byte[] order_buffer)
    {
        char s1=0,s2=0,s3=0;
        int  temp=0;
        char Num1[] = new char[6];		//密文
        char Num2[] = new char[7];		//参数区
        char Num3[] = new char[2];		//存储p,q
        char Qr_SrcString[] = new char[SrcString.length()];

        int i=0;
        int j=0;
        int k=0;
        int p=0,q=0,n=0,r=0,e=0,d=0;
        boolean Flag = false;


        for(i = 0; i < SrcString.length(); i++ )				 //将字符串存放在数组中
        {
            Qr_SrcString[i] = SrcString.charAt(i);
        }


        for (i = 0; i < Qr_SrcString.length; i++)				//提取明文位置
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i+1];
            if((s1== '/') && (s2== '<'))
            {
                temp = i;
                break;
            }
        }

        for (i = (temp+1); i < (Qr_SrcString.length-2); i++)					//提取明文
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i+1];
            s3 = Qr_SrcString[i+2];
            if(j<6)
            {
                if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
                {
                    Num1[j] =(char) ((s2-'0')*10+s3-'0');
                    j++;
                }
                else if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3== ','))
                {
                    Num1[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3== '>'))
                {
                    Num1[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== '<')&&(s2>= '0' && s2<= '9')&&(s3== ','))
                {
                    Num1[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== '<')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
                {
                    Num1[j] =(char) ((s2-'0')*10+s3-'0');
                    j++;
                }
            }
        }


        j = 0;

        for (i = 1; i < (Qr_SrcString.length-2); i++)										//提取参数
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i+1];
            s3 = Qr_SrcString[i+2];
            if(j<7)
            {
                if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
                {
                    Num2[j] =(char) ((s2-'0')*10+s3-'0');
                    j++;
                }
                else if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3== ','))
                {
                    Num2[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== ',')&&(s2>= '0' && s2<= '9')&&(s3== '>'))
                {
                    Num2[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== '<')&&(s2>= '0' && s2<= '9')&&(s3== ','))
                {
                    Num2[j] = (char) (s2-'0');
                    j++;
                }
                else if((s1== '<')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
                {
                    Num2[j] =(char) ((s2-'0')*10+s3-'0');
                    j++;
                }
            }
        }

        BubbleSort(Num2, 7);

        i=0;

        //找出最大且不相等的两个质数
        for(i=7;i>0;i--)
        {
            if(isprime(Num2[i-1])==true)
            {
                if(k<2)
                {
                    Num3[k] = Num2[i-1];
                    k++;
                }
            }
        }


        p = Num3[0];
        q = Num3[1];

        n = p * q;
        r = (p-1) * (q-1);

        for(e=2;e<r;e++)																		//得到 d
        {
            if((r%e)!=0)
            {
                for(d=1;d<65536;d++)
                {
                    if(((e*d)%r)==1)
                    {
                        Flag = true;
                        break;
                    }
                }
            }
            if(Flag) break;
        }

        order_buffer[0] = (byte) (candp(Num1[0],d,n)%255);
        order_buffer[1] = (byte) (candp(Num1[1],d,n)%255);
        order_buffer[2] = (byte) (candp(Num1[2],d,n)%255);
        order_buffer[3] = (byte) (candp(Num1[3],d,n)%255);
        order_buffer[4] = (byte) (candp(Num1[4],d,n)%255);
        order_buffer[5] = (byte) (candp(Num1[5],d,n)%255);


        for(i = 0 ; i < 6; i++)
        {
            System.out.println(BToH((char)(order_buffer[i])));
        }

    }


    // a = 待加密数字或解密数字
    // b = e:加密 d:解密
    // c = n
    //返回值 = 加密结果
    static int candp(int a,int b,int c)
    {
        int r=1;
        b=b+1;
        while(b!=1)
        {
            r=r*a;
            r=r%c;
            b--;
        }
        return r;
    }

    static void BubbleSort (char[] pData,int Count)
    {
        char iTemp;
        for(int i=1; i<Count; i++)
        {
            for (int j=Count-1; j>=1; j--)
            {
                if (pData[j]<pData[j-1])
                {
                    iTemp=pData[j-1];
                    pData[j-1]=pData[j];
                    pData[j]=iTemp;
                }
            }
        }
    }



    //判断一个数是否是质数
    boolean isprime(char a)
    {
        for(int i=2;i<=Math.sqrt((double)a);i++)
            if(a%i==0) return false;
        return true;
    }



    /****************************************************************************************************
     CRC
     ****************************************************************************************************/

    //CRC     (<AaBbCcDd>/<g(x)=x16+x15+x2+1>)

    private   byte[] data = {0x41,0x61,0x42,0x62};

    public  void CRC_Code(String SrcString,byte[] order_buffer)
    {
        char s = 0;
        char s1=0,s2=0,s3=0;
        char temp = 0;
        char[] buf = new char[4];
        char[] num = new char[3];
        int i=0;
        int j=0;
        char PolyCode = 0;

        char  CRC = 0xFFFF;   //CRC寄存器
        char  CRC2 = 0;

        for (i = 0; i < SrcString.length(); i++)				//获取四个明文字符
        {
            s = SrcString.charAt(i);
            if((s>= 'a' && s<= 'z') || (s>='A' && s<= 'Z'))
            {
                temp = (char) i;
                break;
            }
        }

        for (i = temp; i < (temp+4); i++)						//得到前四个原始数据
        {
            buf[i-temp] = SrcString.charAt(i);
        }

        //提取多项式码
        for (i = 0; i < SrcString.length(); i++)
        {
            s1 = SrcString.charAt(i);
            if(s1=='x')
            {
                //s1 = SrcString.charAt(i);
                s2 = SrcString.charAt(i+1);
                s3 = SrcString.charAt(i+2);
                if(j<3)
                {
                    if((s1=='x')&&(s2>= '0' && s2<= '9')&&(s3=='+'))
                    {
                        num[j] = (char) (s2-'0');
                        j++;
                    }
                    else if((s1=='x')&&(s2>= '0' && s2<= '9')&&(s3>= '0' && s3<= '9'))
                    {
                        num[j] = (char) ((s2-'0')*10+s3-'0');
                        j++;
                    }
                }
            }
        }

        PolyCode = (char) (0x0001+(0x0001<<(num[1]))+(0x0001<<(num[2])));		//得到多项式

        System.out.println(BToH(PolyCode));

        System.out.println(BToH(WORD_WordInvert(PolyCode)));

        for(j=0;j<4;j++)
        {
            CRC =  (char) (CRC ^ buf[j]);
            for(i= 0;i<8;i++)
            {
                CRC2 = (char) (CRC & 0x0001);
                if(CRC2 == 0x0001)
                {
                    CRC = (char)((CRC>>1)^ WORD_WordInvert(PolyCode));
                }
                else
                {
                    CRC = (char)(CRC>>1);
                }
            }
        }

        order_buffer[0] = (byte)( (CRC>>8)&0xFF);  	//得到高位

        order_buffer[5] = (byte) (CRC&0xFF);	//得到低位

        System.out.println(BToH(CRC));

        for (i = 0; i < 4; i++)
        {
            order_buffer[i+1] = (byte) buf[i];
        }

        for(i = 0;i < 6; i++)
        {
            System.out.println(BToH((char)((order_buffer[i])&0xFF)));
        }
    }


    private static byte ByteInvert(byte temp)
    {
        char[] sta ={0x00,0x08,0x04,0x0C,0x02,0x0A,0x06,0x0E,0x01,0x09,0x05,0x0D,0x03,0x0B,0x07,0x0F};
        byte d = 0;
        d |= (sta[temp&0xF]) << 4;
        d |= sta[(temp>>4)&0xF];
        return d;
    }

    private static char WORD_WordInvert(char w)
    {
        byte temp = 0;
        char d = 0;

        temp=(byte)(w&0xFF);
        temp=ByteInvert(temp);
        d=(char) ((temp<<8)&0xFF00);
        temp=(byte)((w>>8)&0xFF);
        temp=ByteInvert(temp);
        d|=temp;
        return d;
    }


    /****************************************************************************************************
     仿射密码解密
     ****************************************************************************************************/


    public  void Affine(String SrcString,byte[] order_buffer)
    {
        char s1=0,s2=0;
        char Num1[]= new char[7];//待解密密文
        char Num2[]= new char[7];//解密后明文
        char[] Qr_SrcString = new char[SrcString.length()];
        int i=0;
        int j=0;
        int K1=0,K2=0,K3=0,b=0;;

        char t1[] = { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        char t2[] = { 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };


        for(i = 0; i < SrcString.length(); i++ )				 //将字符串存放在数组中
        {
            Qr_SrcString[i] = SrcString.charAt(i);
        }



        for (i = 0; i < Qr_SrcString.length; i++)				//提取出字符串中前7个大写字母
        {
            s1 = Qr_SrcString[i];
            if((s1>= 'A') && (s1<= 'Z'))
            {
                if(j<7)
                {
                    Num1[j] = s1;
                    j++;
                }
            }
        }



        for (i = 0; i < Qr_SrcString.length; i++)
        {
            s1 = Qr_SrcString[i];
            if((s1== '0') | (s1== '3')| (s1== '5')| (s1== '7')| (s1== '9'))
            {
                K1 = s1-'0';
                break;
            }
        }

        for (i = Qr_SrcString.length; i > 0; i--)
        {
            s2 = Qr_SrcString[i-1];
            if((s2>= '0') && (s2<= '9'))
            {
                K2 = s2-'0';
                break;
            }
        }

        while(((K1*K3)%26)!=1)
        {
            if(K3>65535) {return;}
            K3++;
        }


        for (int n = 0; n < 7; n++)
        {
            for (int l = 0; l < 26; l++)
            {
                if (Num1[n] == t2[l])
                {
                    if(l<K2)
                        b = 26-((K3 *(K2 - l)) % 26);
                    else
                        b = (K3 *(l - K2)) % 26;

                    Num2[n] = t1[b];
                }
            }
        }



        for(i = 0;i<6;i++)
        {
            if((i%2)==0)
            {
                order_buffer[i] = (byte) ((Math.abs(Num2[i+1]-Num2[i]))%255);

            }
            else
            {
                order_buffer[i] = (byte) ((Math.abs(Num2[i+1]+Num2[i]))%255);
            }
        }

        for(i =0; i<6; i++)
        {
            System.out.println(BToH((char)(order_buffer[i]&0xFF)));

        }


    }

    /**********************************************************************************
     * 							数据处理
     * *******************************************************************************/
    // 二进制转十六进制
    public String BToH(char a)
    {
        String b = Integer.toHexString(a);
        return b;
    }



    //将字符串转十进制
    public static int SToB(String a)
    {
        int b = Integer.parseInt(a);

        System.out.println("STOB"+b);
        return b;
    }

    //字符串截取
    public String cut_out(String data,int start,int end)
    {
        String Temp_data = null;
        Temp_data =data.substring(start, end);
        return Temp_data;
    }

}
