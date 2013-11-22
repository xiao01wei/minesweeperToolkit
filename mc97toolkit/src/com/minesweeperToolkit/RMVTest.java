package com.minesweeperToolkit;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class RMVTest
{
	private static int byteToInt(byte b)
	{
		if(b < 0)
		{
			return b + 256;
		}
		return b & 0xff;
	}
	
	private static String add(int i)
	{
		if(i < 10)
		{
			return "00" + i;
		}
		else if(i < 100)
		{
			return "0" + i;
		}
		else
		{
			return "" + i;
		}
	}
	private static int square_size = 16; // 每个格子16个像素?
	public static void print_event(RMVEvent e)
	{
		String event_names[]={"","mv","lc","lr","rc","rr","mc","mr","","pressed","pressedqm","closed",
			"questionmark","flag","blast","boom","won","nonstandard","number0","number1","number2","number3",
			"number4","number5","number6","number7","number8","blast"};
		int c = e.getEvent();
		if(c <= 7) // mouse event
		{
			System.out.println((e.getTime() / 1000) + "." + add(e.getTime() % 1000) + " " + event_names[c] + " "
					+ (e.getX() / square_size + 1) + " " + (e.getY() / square_size + 1) + " ("
					+ e.getX() + " " + e.getY() + ")");
		}
		else if(c <= 14 || (c >= 18 && c <= 27))	// board event
		{
			System.out.println(event_names[c] + " " + e.getX() + " " + e.getY());
		}
		else if(c <= 17) // end event
		{
			System.out.println(event_names[c]);
		}
	}
	
	public static void handle(String name, byte[] byteStream)
	{
		String mvfType = "RMV";
		String userID = Const.INVALID;
		String date = Const.INVALID;
		String level = Const.INVALID;
		String style = Const.INVALID;
		String mode = Const.INVALID;
		String time = Const.INVALID;
		String bbbv = Const.INVALID;
		String bbbvs = Const.INVALID;
		String distance = Const.INVALID;
		String clicks = Const.INVALID;
		String zini = Const.INVALID;
		String rqp = Const.INVALID;
		String ioe = Const.INVALID;
		String completion = Const.INVALID;
		String num0 = Const.INVALID;
		String num1 = Const.INVALID;
		String num2 = Const.INVALID;
		String num3 = Const.INVALID;
		String num4 = Const.INVALID;
		String num5 = Const.INVALID;
		String num6 = Const.INVALID;
		String num7 = Const.INVALID;
		String num8 = Const.INVALID;
		String numAll = Const.INVALID;
		String disSpeed = Const.INVALID;
		String openings = Const.INVALID;
		String allClicks = Const.INVALID;
		String disBv = Const.INVALID;
		String disNum = Const.INVALID;
		String hzoe = Const.INVALID;
		String numSpeed = Const.INVALID;
		String zinis = Const.INVALID;
		String occam = Const.INVALID;
		String lclicks = Const.INVALID;
		String dclicks = Const.INVALID;
		String rclicks = Const.INVALID;
		String qg = Const.INVALID;
		String flags = Const.INVALID;
		String islands = Const.INVALID;
		String markFlag = Const.INVALID;
		String hold = Const.INVALID;

		String level_names[] = {"beginner", "intermediate", "expert", "custom"};
		String mode_names[] = {"classic", "UPK", "cheat", "density"};

		int event_index = 0;
		int cur_index = 0;
		int square_size = 16; // 每个格子16个像素?
		List<RMVEvent> eventList = new ArrayList<RMVEvent>();
		// header 1
		if(null == byteStream || byteToInt(byteStream[0]) != '*' || (!(byteToInt(byteStream[1]) == 'r' && 
				byteToInt(byteStream[2]) == 'm' && byteToInt(byteStream[3]) == 'v') && !(byteToInt(byteStream[1]) == 'u'
				&& byteToInt(byteStream[2]) == 'm' && byteToInt(byteStream[3]) == 'f'))) // 前4个字节是*rmv或者*umf
		{
			System.out.println("error1");
			return;
		}
		cur_index += 4; // =4;
		// 下面两个字节(5和6)是00,01,否则不合法
		if('\u0000' != byteToInt(byteStream[cur_index]) || '\u0001' != byteToInt(byteStream[cur_index + 1]))
		{
			System.out.println("error2");
			return;
		}
		cur_index += 2; // =6;
		// 7,8,9,10没用?
		cur_index += 4; // =10;
		// header 2
		int result_info_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]); // 有效信息长度
		cur_index += 2; // =12;
		int version_info_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]); // 版本信息长度
		cur_index += 2; // =14;
		int player_info_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]); // 个人信息长度
		cur_index += 2; // =16;
		int board_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]); // board大小,貌似没用
		cur_index += 2; // =18;
		int preflags_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
		cur_index += 2; // =20;
		int properties_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
		cur_index += 2; // =22;
		int vid_size = byteToInt(byteStream[cur_index]) * 65536 + byteToInt(byteStream[cur_index + 1]) * 16777216
				+ byteToInt(byteStream[cur_index + 2]) + byteToInt(byteStream[cur_index + 3]) * 256;
		cur_index += 4; // =26;
		int cs_size = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
		cur_index += 2; // =28;
		// newline
		// 第28个字节是换行,跳过
		cur_index += 1; // =29;
		int xx;
		String program = new String();
		// result string
		// 下标从29到result_info_size+27;
		for(xx = 0; xx < result_info_size - 1; xx++)
		{
			cur_index += 1;
		}
		// = result_info_size + 28;
		// version info
		// 版本信息
		for(xx = 0; xx < version_info_size - 1; xx++)
		{
			program = new String(byteStream, cur_index, version_info_size - 1);
		}
		cur_index += version_info_size - 1; // = result_info_size +
											// version_info_size + 27;
		// a weird character 0x2E
		// 跳过一个字符,但貌似不是2e?
		cur_index += 1; // = result_info_size + version_info_size + 28;
		// player info
		// 用户信息
		int num_player_info = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
		cur_index += 2; // = result_info_size + version_info_size + 30;
		// 用户id长度
		int name_length = 0;
		if(num_player_info > 0)
		{
			name_length = byteToInt(byteStream[cur_index]);
			cur_index += 1; // = result_info_size + version_info_size + 31;
			userID = new String(byteStream, cur_index, name_length);
			cur_index += name_length; // = result_info_size +
										// version_info_size + 31 +
										// name_length;
			// i hope i got this correctly; all videos i have don't have
			// this part
			// 不知道啥意思,上面是原文,可能是为了兼容未来的版本?可以多人合作?于是有多个ID?
			// 一般不会执行
			for(int xy = 1; xy < num_player_info; ++xy)
			{
				name_length = byteToInt(byteStream[cur_index]);
				cur_index += 1;
				for(int xz = 0; xz < name_length; ++xz) // 跳过字符
				{

				}
				cur_index += name_length;
			}
		}
		// board
		// 跳过4个字节
		cur_index += 4; // = result_info_size + version_info_size + 39 +
						// name_length;
		int board_width = byteToInt(byteStream[cur_index]);
		cur_index += 1; // = result_info_size + version_info_size + 40 +
						// name_length;
		int board_height = byteToInt(byteStream[cur_index]);
		cur_index += 1; // = result_info_size + version_info_size + 41 +
						// name_length;
		int board_mine = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
		cur_index += 2; // = result_info_size + version_info_size + 43 +
						// name_length;

		int[] board = new int[board_width * board_height];
		// 布局,先置零
		for(int xy = 0; xy < board_width * board_height; xy++)
		{
			board[xy] = 0;
		}
		// 布置雷
		for(int xy = 0; xy < board_mine; xy++)
		{
			int board_c = byteToInt(byteStream[cur_index]);
			cur_index++;
			int board_r = byteToInt(byteStream[cur_index]);
			cur_index++;
			if(board_c > board_width || board_r > board_height)
			{
				System.out.println("error3");
				return;
			}
			board[board_r * board_width + board_c] = 1;
		}
		if(preflags_size > 0)
		{
			int num_preflags = byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]);
			cur_index += 2;
			for(int xy = 0; xy < num_preflags; ++xy)
			{
				int board_c = byteToInt(byteStream[cur_index]);
				cur_index++;
				int board_r = byteToInt(byteStream[cur_index]);
				cur_index++;

				RMVEvent e = new RMVEvent();
				e.setEvent('\u0004');
				e.setX(square_size / 2 + board_c * square_size);
				e.setY(square_size / 2 + board_r * square_size);
				e.setTime(0);
				eventList.add(e);
				event_index++;

				RMVEvent e2 = new RMVEvent();
				e2.setEvent('\u0005');
				e2.setX(square_size / 2 + board_c * square_size);
				e2.setY(square_size / 2 + board_r * square_size);
				e2.setTime(0);
				eventList.add(e2);
				event_index++;
			}
		}

		// properties	3_tkolar 分别是(16进制)0,1,0,0,后面是3,0,0,0,0,0,17,0,40,9
		int qm = byteToInt(byteStream[cur_index]);
		cur_index += 1;
		int nf = byteToInt(byteStream[cur_index]);
		cur_index += 1;
		int rmode = byteToInt(byteStream[cur_index]);
		cur_index += 1;
		int rlevel = byteToInt(byteStream[cur_index]);
		cur_index += 1;
		int ii;
		for(ii = 4; ii < properties_size; ++ii)
		{
			cur_index += 1; // 跳过
		}

		boolean isFirstEvent = true;

		// events: gogogo
		while(true)
		{
			RMVEvent ee = new RMVEvent();
			int yy = byteToInt(byteStream[cur_index]);
			cur_index++;
			ii++;
			ee.setEvent(yy);
			
			// timestamp event
			if(0 == yy)
			{
				cur_index += 4;
				ii += 4;
				eventList.add(ee);
			}
			else if(yy <= 7) // mouse event
			{
				ii += 8;
				ee.setTime(byteToInt(byteStream[cur_index]) * 65536 + byteToInt(byteStream[cur_index + 1]) * 256
						+ byteToInt(byteStream[cur_index + 2]));
				cur_index += 3;
				cur_index++;

				ee.setX(byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]) - 12);
				cur_index += 2;
				ee.setY(byteToInt(byteStream[cur_index]) * 256 + byteToInt(byteStream[cur_index + 1]) - 56);
				cur_index += 2;

				RMVEvent ee2 = new RMVEvent();
				if(isFirstEvent)
				{
				//	isFirstEvent = false;
					ee2.setEvent(ee.getEvent());
					ee.setEvent(2);
					ee2.setTime(ee.getTime());
					ee2.setX(ee.getX());
					ee2.setY(ee.getY());
				}
				eventList.add(ee);
				if(isFirstEvent)
				{
					isFirstEvent = false;
					eventList.add(ee2);
				}
			}
			else if(yy == 8)
			{
				eventList.add(ee);
				System.out.println("error4");
				break;
			}
			// board event
			else if(yy <= 14 || (yy >= 18 && yy <= 27))
			{
				ii += 2;
				ee.setX(byteToInt(byteStream[cur_index]) + 1);
				cur_index++;
				ee.setY(byteToInt(byteStream[cur_index]) + 1);
				cur_index++;

				eventList.add(ee);
			}
			else if(yy <= 17) // end event
			{
				eventList.add(ee);
				System.out.println("file end");
				break;
			}
			else
			{
				eventList.add(ee);
				System.out.println("error5");
				break;
			}
		}
		int size = eventList.size();
		
		System.out.println("version: Rev5");
		System.out.println("program: " + program);
		System.out.println("name: " + userID);
		System.out.println("level: " + level_names[rlevel]);
		System.out.println("width: " + board_width);
		System.out.println("height: " + board_height);
		System.out.println("mines: " + board_mine);
		System.out.println("mode: " + mode_names[rmode]);
		if(0 != qm)
		{
			System.out.println("marks: on");
		}
		
		System.out.println("Board: ");
		for(int zx = 0; zx < board_height; ++zx)
		{
			for(int zy = 0; zy < board_width;++zy)
			{
				if(0 != board[zx * board_width + zy])
				{
					System.out.print("*");
				}
				else
				{
					System.out.print("0");
				}
			}
			System.out.println();
		}
		System.out.println("Events:");
		System.out.println("0.000 start");
		for(int zx = 0; zx < size; ++zx)
		{
			print_event(eventList.get(zx));
		}
	}

	private static void print_file(String name)
	{
		File file = null;
		FileInputStream fis = null;
		byte[] stream = null;
		try
		{
			file = new File(name);
			long file_length = file.length();
			if(file_length > Integer.MAX_VALUE)
			{
				System.out.println("error0: file too big");
				return;
			}
			int fileLength = (int)file_length;
			stream = new byte[fileLength];
			fis = new FileInputStream(file);
			int index = -1;
			int ix = 0;
			if(-1 != (index = fis.read(stream)))
			{
				handle("good", stream);
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			if(null != fis)
			{
				try
				{
					fis.close();
				}
				catch(Exception e)
				{

				}
				fis = null;
			}
		}
	}

	public static void main(String[] args)
	{
		print_file("e:/ss1/3_tkolar.rmv");
//		print_file("d:/rmvtest/3.rmv");
	}
}
