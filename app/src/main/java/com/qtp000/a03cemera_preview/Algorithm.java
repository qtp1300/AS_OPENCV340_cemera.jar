package com.qtp000.a03cemera_preview;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by 祁天培 on 2018/2/9.
 */

public class Algorithm {

    static private int[] temp = new int[100];		//点杠
    static private int temp_num = 0;		//temp的数据位置
    static private char[][] char_a = new char[7][5];
    static private String[] strings = new String[7];
    static private int i_string = 0;
    static private int char_string = 0;
    static private char[] char_null = new char[3];
    static private char[] char_unlocked = new char[6];
    static private char[] char_preunlocked = new char[7];
    static private int[] int_unlocked = new int[6];
    static private char[] char_reversal = new char[6];
    static private int num_char_unlocked = 0;

    static int[] finall = new int[6];
    static int num_mosi = 0;


    /****************************************************************************************************
     RSA
     ****************************************************************************************************/

    //RSA解密
    public void RSA_Code(String SrcString, byte[] order_buffer) {
        char s1 = 0, s2 = 0, s3 = 0;
        int temp = 0;
        char Num1[] = new char[6];        //密文
        char Num2[] = new char[7];        //参数区
        char Num3[] = new char[2];        //存储p,q
        char Qr_SrcString[] = new char[SrcString.length()];

        int i = 0;
        int j = 0;
        int k = 0;
        int p = 0, q = 0, n = 0, r = 0, e = 0, d = 0;
        boolean Flag = false;


        for (i = 0; i < SrcString.length(); i++)                 //将字符串存放在数组中
        {
            Qr_SrcString[i] = SrcString.charAt(i);
        }


        for (i = 0; i < Qr_SrcString.length; i++)                //提取明文位置
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i + 1];
            if ((s1 == '/') && (s2 == '<')) {
                temp = i;
                break;
            }
        }

        for (i = (temp + 1); i < (Qr_SrcString.length - 2); i++)                    //提取明文
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i + 1];
            s3 = Qr_SrcString[i + 2];
            if (j < 6) {
                if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 >= '0' && s3 <= '9')) {
                    Num1[j] = (char) ((s2 - '0') * 10 + s3 - '0');
                    j++;
                } else if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 == ',')) {
                    Num1[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 == '>')) {
                    Num1[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == '<') && (s2 >= '0' && s2 <= '9') && (s3 == ',')) {
                    Num1[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == '<') && (s2 >= '0' && s2 <= '9') && (s3 >= '0' && s3 <= '9')) {
                    Num1[j] = (char) ((s2 - '0') * 10 + s3 - '0');
                    j++;
                }
            }
        }


        j = 0;

        for (i = 1; i < (Qr_SrcString.length - 2); i++)                                        //提取参数
        {
            s1 = Qr_SrcString[i];
            s2 = Qr_SrcString[i + 1];
            s3 = Qr_SrcString[i + 2];
            if (j < 7) {
                if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 >= '0' && s3 <= '9')) {
                    Num2[j] = (char) ((s2 - '0') * 10 + s3 - '0');
                    j++;
                } else if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 == ',')) {
                    Num2[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == ',') && (s2 >= '0' && s2 <= '9') && (s3 == '>')) {
                    Num2[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == '<') && (s2 >= '0' && s2 <= '9') && (s3 == ',')) {
                    Num2[j] = (char) (s2 - '0');
                    j++;
                } else if ((s1 == '<') && (s2 >= '0' && s2 <= '9') && (s3 >= '0' && s3 <= '9')) {
                    Num2[j] = (char) ((s2 - '0') * 10 + s3 - '0');
                    j++;
                }
            }
        }

        BubbleSort(Num2, 7);

        i = 0;

        //找出最大且不相等的两个质数
        for (i = 7; i > 0; i--) {
            if (isprime(Num2[i - 1]) == true) {
                if (k < 2) {
                    Num3[k] = Num2[i - 1];
                    k++;
                }
            }
        }


        p = Num3[0];
        q = Num3[1];

        n = p * q;
        r = (p - 1) * (q - 1);

        for (e = 2; e < r; e++)                                                                        //得到 d
        {
            if ((r % e) != 0) {
                for (d = 1; d < 65536; d++) {
                    if (((e * d) % r) == 1) {
                        Flag = true;
                        break;
                    }
                }
            }
            if (Flag) break;
        }

        order_buffer[0] = (byte) (candp(Num1[0], d, n) % 255);
        order_buffer[1] = (byte) (candp(Num1[1], d, n) % 255);
        order_buffer[2] = (byte) (candp(Num1[2], d, n) % 255);
        order_buffer[3] = (byte) (candp(Num1[3], d, n) % 255);
        order_buffer[4] = (byte) (candp(Num1[4], d, n) % 255);
        order_buffer[5] = (byte) (candp(Num1[5], d, n) % 255);


        for (i = 0; i < 6; i++) {
            System.out.println(BToH((char) (order_buffer[i])));
        }

    }


    // a = 待加密数字或解密数字
    // b = e:加密 d:解密
    // c = n
    //返回值 = 加密结果
    static int candp(int a, int b, int c) {
        int r = 1;
        b = b + 1;
        while (b != 1) {
            r = r * a;
            r = r % c;
            b--;
        }
        return r;
    }

    static void BubbleSort(char[] pData, int Count) {
        char iTemp;
        for (int i = 1; i < Count; i++) {
            for (int j = Count - 1; j >= 1; j--) {
                if (pData[j] < pData[j - 1]) {
                    iTemp = pData[j - 1];
                    pData[j - 1] = pData[j];
                    pData[j] = iTemp;
                }
            }
        }
    }


    //判断一个数是否是质数
    boolean isprime(char a) {
        for (int i = 2; i <= Math.sqrt((double) a); i++)
            if (a % i == 0) return false;
        return true;
    }


    /****************************************************************************************************
     CRC
     ****************************************************************************************************/

    //CRC     (<AaBbCcDd>/<g(x)=x16+x15+x2+1>)

    private byte[] data = {0x41, 0x61, 0x42, 0x62};

    public void CRC_Code(String SrcString, byte[] order_buffer) {
        char s = 0;
        char s1 = 0, s2 = 0, s3 = 0;
        char temp = 0;
        char[] buf = new char[4];
        char[] num = new char[3];
        int i = 0;
        int j = 0;
        char PolyCode = 0;

        char CRC = 0xFFFF;   //CRC寄存器
        char CRC2 = 0;

        for (i = 0; i < SrcString.length(); i++)                //获取四个明文字符
        {
            s = SrcString.charAt(i);
            if ((s >= 'a' && s <= 'z') || (s >= 'A' && s <= 'Z')) {
                temp = (char) i;
                break;
            }
        }

        for (i = temp; i < (temp + 4); i++)                        //得到前四个原始数据
        {
            buf[i - temp] = SrcString.charAt(i);
        }

        //提取多项式码
        for (i = 0; i < SrcString.length(); i++) {
            s1 = SrcString.charAt(i);
            if (s1 == 'x') {
                //s1 = SrcString.charAt(i);
                s2 = SrcString.charAt(i + 1);
                s3 = SrcString.charAt(i + 2);
                if (j < 3) {
                    if ((s1 == 'x') && (s2 >= '0' && s2 <= '9') && (s3 == '+')) {
                        num[j] = (char) (s2 - '0');
                        j++;
                    } else if ((s1 == 'x') && (s2 >= '0' && s2 <= '9') && (s3 >= '0' && s3 <= '9')) {
                        num[j] = (char) ((s2 - '0') * 10 + s3 - '0');
                        j++;
                    }
                }
            }
        }

        PolyCode = (char) (0x0001 + (0x0001 << (num[1])) + (0x0001 << (num[2])));        //得到多项式

        System.out.println(BToH(PolyCode));

        System.out.println(BToH(WORD_WordInvert(PolyCode)));

        for (j = 0; j < 4; j++) {
            CRC = (char) (CRC ^ buf[j]);
            for (i = 0; i < 8; i++) {
                CRC2 = (char) (CRC & 0x0001);
                if (CRC2 == 0x0001) {
                    CRC = (char) ((CRC >> 1) ^ WORD_WordInvert(PolyCode));
                } else {
                    CRC = (char) (CRC >> 1);
                }
            }
        }

        order_buffer[0] = (byte) ((CRC >> 8) & 0xFF);    //得到高位

        order_buffer[5] = (byte) (CRC & 0xFF);    //得到低位

        System.out.println(BToH(CRC));

        for (i = 0; i < 4; i++) {
            order_buffer[i + 1] = (byte) buf[i];
        }

        for (i = 0; i < 6; i++) {
            System.out.println(BToH((char) ((order_buffer[i]) & 0xFF)));
        }
    }

    /*摩斯电码算法——祁天培添加*/
    public void MOSI_Code(String SrcString, byte[] order_buffer,byte[] order_buffer2) {

        i_string = 0;
        char_string = 0;
        temp_num = 0;
        num_char_unlocked = 0;




        if(SrcString != null && SrcString != "") {
            char[] input_char = SrcString.toCharArray();
            System.out.println(input_char);
            /*分离出.--.-.-..--.-.-.-.-.-.-.--..-.-.-.--.-.-*/
            for (int i = 0; i < input_char.length; i++) {
                if (i <= input_char.length) {
                    if (input_char[i] == '-' || input_char[i] == '.') {
                        temp[temp_num++] = i;
                        char_a[i_string][char_string++] = input_char[i];
                        if (input_char[i + 1] != '-' && input_char[i + 1] != '.') {
                            i_string++;
                            char_string = 0;
                        }
                    }
                }
            }
            for (char[] c : char_a) {
                System.out.println("截取" + new String(c));
            }
            /*除去空字符*/
            for (int i = 0; i < char_a.length; i++) {
                strings[i] = "";
                for (int j = 0; j < 5; j++) {
                    if (char_a[i][j] != char_null[1]) {
                        strings[i] = strings[i] + char_a[i][j];
                    }
                }
            }
            for (String s : strings) {
                System.out.println("去空" + s);
            }
            /*解码*/
            for (int i = 0; i < strings.length; i++) {
                char_preunlocked[i] = unlock(strings[i]);
            }

            System.out.println("解码" + new String(char_preunlocked));

            numorchar();    //分离数字和字符
            char_unlocked = sort(char_unlocked);    //排序
            System.out.println("排序" + new String(char_unlocked));

            /*遍历赋值输出*/
            for (int i = 0; i < char_unlocked.length; i++) {
                int_unlocked[i] = (int) char_unlocked[i];
                //			System.out.println("0x"+Integer.toHexString(int_unlocked[i]));
            }

            //计算
            finall[0] = int_unlocked[0] + int_unlocked[1];
            finall[1] = int_unlocked[0] >= int_unlocked[1] ? int_unlocked[0] - int_unlocked[1] : int_unlocked[1] - int_unlocked[0];
            finall[2] = int_unlocked[2] + int_unlocked[3];
            finall[3] = int_unlocked[2] >= int_unlocked[3] ? int_unlocked[2] - int_unlocked[3] : int_unlocked[3] - int_unlocked[2];
            finall[4] = int_unlocked[4] + int_unlocked[5];
            finall[5] = int_unlocked[4] >= int_unlocked[5] ? int_unlocked[4] - int_unlocked[5] : int_unlocked[5] - int_unlocked[4];
            order_buffer[0] = (byte) finall[0];
            order_buffer[1] = (byte) finall[1];
            order_buffer[2] = (byte) finall[2];
            order_buffer[3] = (byte) finall[3];
            order_buffer[4] = (byte) finall[4];
            order_buffer[5] = (byte) finall[5];

            Log.e("光照是", Integer.toBinaryString(num_mosi));
            order_buffer2[0] = (byte) (char) num_mosi;


            String[] str_out = SrcString.split("0x");
            order_buffer2[1] = (byte) Integer.parseInt(str_out[1],16);
            Log.e("光照",Integer.toString(order_buffer2[0]));
            Log.e("RFID",Integer.toString(order_buffer2[1]));

        }

    }


    private static void numorchar() {
        for (int i = 0; i < char_preunlocked.length; i++) {
            if ((int)char_preunlocked[i] > 58 || (int)char_preunlocked[i] <47) {
//				System.out.println((int)char_preunlocked[i]);
                char_unlocked[num_char_unlocked] = char_preunlocked[i];
                num_char_unlocked++;
            }
            else {
                num_mosi = Integer.parseInt(new Character(char_preunlocked[i]).toString());
            }
        }
    }
    private static char[] sort(char[] in) {
        Arrays.sort(in);
        if((num_mosi % 2) == 0){
            for (int i = 0; i < char_unlocked.length; i++) {
                char_reversal[i] = char_unlocked[5-i];
            }
            return char_reversal;
        }
        return in;

    }

    private static char unlock(String str){
        switch (str) {
            case ".-":
                //System.out.print("A");
                return 'A';
            case "-...":
                //System.out.print("B");
                return 'B';
            case "-.-.":
                //System.out.print("C");
                return 'C';
            case "-..":
                //System.out.print("D");
                return 'D';
            case ".":
                //System.out.print("E");
                return 'E';
            case "..-.":
                //System.out.print("F");
                return 'F';
            case "--.":
                //System.out.print("G");
                return 'G';
            case "....":
                //System.out.print("H");
                return 'H';
            case "..":
                //System.out.print("I");
                return 'I';
            case ".---":
                //System.out.print("J");
                return 'J';
            case "-.-":
                //System.out.print("K");
                return 'K';
            case ".-..":
                //System.out.print("L");
                return 'L';
            case "--":
                //System.out.print("M");
                return 'M';
            case "-.":
                //System.out.print("N");
                return 'N';
            case "---":
                //System.out.print("O");
                return 'O';
            case ".--.":
                //System.out.print("P");
                return 'P';
            case "--.-":
                //System.out.print("Q");
                return 'Q';
            case ".-.":
                //System.out.print("R");
                return 'R';
            case "...":
                //System.out.print("S");
                return 'S';
            case "-":
                //System.out.print("T");
                return 'T';
            case "..-":
                //System.out.print("U");
                return 'U';
            case "...-":
                //System.out.print("V");
                return 'V';
            case ".--":
                //System.out.print("W");
                return 'W';
            case "-..-":
                //System.out.print("X");
                return 'X';
            case "-.--":
                //System.out.print("Y");
                return 'Y';
            case "--..":
                //System.out.print("Z");
                return 'Z';
            case ".----":
                //System.out.print("Z");
                return '1';
            case "..---":
                //System.out.print("Z");
                return '2';
            case "...--":
                //System.out.print("Z");
                return '3';
            case "....-":
                //System.out.print("Z");
                return '4';
            case ".....":
                //System.out.print("Z");
                return '5';
            case "-....":
                //System.out.print("Z");
                return '6';
            case "--...":
                //System.out.print("Z");
                return '7';
            case "---..":
                //System.out.print("Z");
                return '8';
            case "----.":
                //System.out.print("Z");
                return '9';
            case "-----":
                //System.out.print("Z");
                return '0';
            default:
                return 0;
        }
    }


    private static byte ByteInvert(byte temp) {
        char[] sta = {0x00, 0x08, 0x04, 0x0C, 0x02, 0x0A, 0x06, 0x0E, 0x01, 0x09, 0x05, 0x0D, 0x03, 0x0B, 0x07, 0x0F};
        byte d = 0;
        d |= (sta[temp & 0xF]) << 4;
        d |= sta[(temp >> 4) & 0xF];
        return d;
    }

    private static char WORD_WordInvert(char w) {
        byte temp = 0;
        char d = 0;

        temp = (byte) (w & 0xFF);
        temp = ByteInvert(temp);
        d = (char) ((temp << 8) & 0xFF00);
        temp = (byte) ((w >> 8) & 0xFF);
        temp = ByteInvert(temp);
        d |= temp;
        return d;
    }


    /****************************************************************************************************
     仿射密码解密
     ****************************************************************************************************/


    public void Affine(String SrcString, byte[] order_buffer) {
        char s1 = 0, s2 = 0;
        char Num1[] = new char[7];//待解密密文
        char Num2[] = new char[7];//解密后明文
        char[] Qr_SrcString = new char[SrcString.length()];
        int i = 0;
        int j = 0;
        int K1 = 0, K2 = 0, K3 = 0, b = 0;
        ;

        char t1[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char t2[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


        for (i = 0; i < SrcString.length(); i++)                 //将字符串存放在数组中
        {
            Qr_SrcString[i] = SrcString.charAt(i);
        }


        for (i = 0; i < Qr_SrcString.length; i++)                //提取出字符串中前7个大写字母
        {
            s1 = Qr_SrcString[i];
            if ((s1 >= 'A') && (s1 <= 'Z')) {
                if (j < 7) {
                    Num1[j] = s1;
                    j++;
                }
            }
        }


        for (i = 0; i < Qr_SrcString.length; i++) {
            s1 = Qr_SrcString[i];
            if ((s1 == '0') | (s1 == '3') | (s1 == '5') | (s1 == '7') | (s1 == '9')) {
                K1 = s1 - '0';
                break;
            }
        }

        for (i = Qr_SrcString.length; i > 0; i--) {
            s2 = Qr_SrcString[i - 1];
            if ((s2 >= '0') && (s2 <= '9')) {
                K2 = s2 - '0';
                break;
            }
        }

        while (((K1 * K3) % 26) != 1) {
            if (K3 > 65535) {
                return;
            }
            K3++;
        }


        for (int n = 0; n < 7; n++) {
            for (int l = 0; l < 26; l++) {
                if (Num1[n] == t2[l]) {
                    if (l < K2)
                        b = 26 - ((K3 * (K2 - l)) % 26);
                    else
                        b = (K3 * (l - K2)) % 26;

                    Num2[n] = t1[b];
                }
            }
        }


        for (i = 0; i < 6; i++) {
            if ((i % 2) == 0) {
                order_buffer[i] = (byte) ((Math.abs(Num2[i + 1] - Num2[i])) % 255);

            } else {
                order_buffer[i] = (byte) ((Math.abs(Num2[i + 1] + Num2[i])) % 255);
            }
        }

        for (i = 0; i < 6; i++) {
            System.out.println(BToH((char) (order_buffer[i] & 0xFF)));

        }


    }

    /**********************************************************************************
     * 							数据处理
     * *******************************************************************************/
    // 二进制转十六进制
    public String BToH(char a) {
        String b = Integer.toHexString(a);
        return b;
    }


    //将字符串转十进制
    public static int SToB(String a) {
        int b = Integer.parseInt(a);

        System.out.println("STOB" + b);
        return b;
    }

    //字符串截取
    public String cut_out(String data, int start, int end) {
        String Temp_data = null;
        Temp_data = data.substring(start, end);
        return Temp_data;
    }

}
