/*
MIT License

Copyright (c) 2018 Alexander Kirichenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package virp;
/* 
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Задача: создать многоконное приложение. На одном окне размещаются кнопки, это окно изображает  
переднюю панель системного блока РС. Второе окно изображает монитор. По нажатию на кнопку первого окна, 
второе окно (монитор) как-нибудь изменяется (на него выводится текст или меняется его цвет и т.п.).
На "панели системного блока" должны быть размещены следующие кнопки: включения РС, его перезагрузки,
кнопка извлечения диска из CD-ROM'а.  
<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
*/

import java.awt.*;
import java.awt.event.*; //подключить библиотеки классов для работы с оконным интерфейсом
import java.util.*; //подключить библиотеку классов утилит (в данной программе используется только класс Random)

public class Virtu_pc implements ActionListener { //класс Virtu_pc реализует абстрактные методы описанные в классе ActionListener   
	Win_one frw;
	Win_two sew; //объектные переменные для первого (frw) и второго (sew) окна
	
	String[] screenst; //массив строк, хранит выводимый при "загрузке" текст
	int sel = 0;	//смещение в массиве, используется для добавления строк в screenst после "перезагрузки" 

	@SuppressWarnings("serial")
	class Win_one extends Frame { //подкласс Win_one наследник стандартного класса Frame
		Button button1, button2, button3; //объекты: первая, вторая и третья кнопки

		boolean rest; //флаг связан с кнопкой "eject"
		
		Win_one() { //конструктор класса Win_one 
			setTitle("PC panel v0.08"); // установить заголовок окна 
			setSize(/* width */320,/* height */320); //его размеры
			setBackground(Color.WHITE); //цвет фона окна
			setLocation(200, 200); //расположения окна
			setResizable(false); //запретить изменение размеров окна

			setLayout(null); //в качестве "менеджера размещения" компонентов в контейнере задать null 
			//(расположение объектов в окне будем задавать вручную)
			button3 = new Button("eject"); //вызвать конструктор стандартного класса Button (создание кнопки)
			button3.setBounds(/* x */245,/* y */75, /* width */55,/* height */25);
			//задать расположение кнопки в окне и её размеры (начало координат в левом верхнем углу окна)

			button2 = new Button("Reset");
			button2.setBounds(138, 205, 45, 25);

			button1 = new Button("Power");
			button1.setBounds(123, 235, 75, 75);
			//аналогично для button2 и button3
			
			add(button1);
			add(button2);
			add(button3);
			//добавить объекты button1,button2 и button3 в контейнер связанный с окном
			
			addWindowListener(new WindowAdapter() {//связать окно с обработчиком события 
				public void windowClosing(WindowEvent ev) { //попытки закрытия окна (щелчок на [Х])
					System.exit(0); //при возникновении такого события завершить приложение
				}
			});

			setVisible(true); //сделать окно видимым
		}

		public void paint(Graphics ren) { //переопределение метода отрисовки окна, унаследованного от Frame 
			ren.setColor(Color.GRAY); //установка цвета для рисования  
			ren.fill3DRect(8, 25, 303, 78, true); //отрисовка объёмного прямоугольника (внешняя граница CD привода)
			ren.draw3DRect(12, 28, 296, 41, true); //лоток привода
			ren.setColor(Color.BLACK); //установка цвета для рисования
			ren.drawLine(13, 48, 126, 48); 
			ren.drawLine(194, 48, 307, 48);
			ren.drawLine(9, 87, 308, 87); //красивые чёрные линии на корпусе "CD-привода"
			ren.setColor(Color.WHITE);  
			ren.setFont(new Font("Serif", Font.BOLD, 16)); //установить шрифт объект font(тип шрифта, начертание, размер) 
			ren.drawString("cdKiller2000", 110, 54); //вывести текст на корпус "привода"
			ren.setFont(new Font("Courier New", Font.PLAIN, 12)); 
			ren.drawString("MultiDrive", 12, 80); //аналогично			
			
			if (rest) { //если была нажата кнопка извлечения диска, то
			 ren.setColor(new Color(0,250,0)); 
			 ren.fillRect(216, 85, 8, 5);	//нарисовать на приводе светло-зелёный прямоугольник
			 try{       //на случай ошибки вызова метода sleep
				   Thread.sleep(700); //подождать 700 милисекунд 
				  }catch(InterruptedException esleep){}
			}
			
			rest = false; //сбросить флаг нажатия на кнопку "eject"
		    ren.setColor(new Color(0,160,0));
			ren.fillRect(216, 85, 8, 5); //нарисовать темно-зелёный прямоугольник на приводе 
		}
	}

	@SuppressWarnings("serial")
	class Win_two extends Frame { //подкласс Win_two наследник стандартного класса Frame
		boolean oned = false; //флаг - компьютер квлючён
		boolean bootr = false; //флаг - компьютер загружается (задержка при выводе текста на окно
		//только после его влючения) 
		Random myrandomize; //генератор случайных чисел (случайные задержки при выводе текста, имитация загрузки)

		Win_two() {
			myrandomize = new Random(); //создать генератор случайных чисел
			setTitle("PC monitor v0.07");
			setSize(/* width */320,/* height */320);
			setLocation(540, 200);
			setBackground(new Color(245, 245, 245));
			setResizable(false);

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent ev) {
					System.exit(0);
				}
			});
			setVisible(true); //аналогично первому окну
		}

		public void paint(Graphics ren) { //переопределение метода отрисовки окна,
			if (oned) { //если "компьютер" включён, то рисовать на мониторе
				ren.setColor(Color.BLACK);
				ren.fillRect(5, 23, 312, 312); //очистить окно (рисую чёрный квадрат)
				ren.setColor(Color.WHITE); //новый цвет (для текста)
				
				for (int i = 0; i<26; i++) { //вывожу массив строк на окно
			     if (bootr && i>0 && i<25) //если "компьютер" загружается и выводится не первая/последняя строка, то
				  try{       
				   Thread.sleep(myrandomize.nextInt(700)); //обождать случайное число (не более 700) милисекунд 
				  }catch(InterruptedException esleep){}
				 if (screenst[i]!=null) //костыль, если вдруг (а такое происходит на последнем шаге цикла) ссылка на массив
					 //стала null, то не пытаться рисовать пустую строку
					 ren.drawString(screenst[i], 8, 33+11*i); 
				}
				bootr = false; //загрузка закончилась, блокировать задержку
			}
		}
				
	}

	Virtu_pc() { //конструктор главного класса
		screenst = new String[26]; //создать строковый массив
		sew = new Win_two();
		frw = new Win_one(); //создать окна
		frw.button1.addActionListener(this);
		frw.button2.addActionListener(this);
		frw.button3.addActionListener(this); //связать с кнопками обработчики реализованные в классе Virtu_pc
	}
	
	void bboot (int str) { //метод наполнения строкового массива красивым текстом
		// его агрумент - позиция с которой надо начинать наполнение массива
		screenst[str] = "POST run...";
		screenst[str+1] = "Java Vitrual TM procesor at <UNDIFINED> MHz";
		screenst[str+2] = "Memory test OK";
		screenst[str+3] = "ERROR Keyboard not found!";
		screenst[str+4] = "PROCEEDED...";
		screenst[str+5] = "POST was complited with 1 ERROR.";
		screenst[str+6] = "";
		screenst[str+7] = "<*QDOS loader*>";
		screenst[str+8] = "Your incredible OS will loaded now...";
		screenst[str+9] = "";
		screenst[str+10] = "FILE config.nt was perfomed";
		screenst[str+11] = "FILE autoexec.reg was performed";
		screenst[str+12] = "START Graphic User Interface";
		screenst[str+13] = "ERROR! Necessary library d3d10.dll not found!";
		screenst[str+14] = "START Normal User Interface OK";
		screenst[str+15] = "Phuuph... ";
		screenst[str+16] = "Quick and Dirty Operating System was loaded!";
		screenst[str+17] = "C:\\>_";
	}
	
	public void actionPerformed(ActionEvent but) { //обработчик событий ActionEvent 
		//(связан и с нажатием на кнопки)
		if (frw.button1 == but.getSource()) //если источником событий является первая кнопка("Power"), то
			if (!sew.oned) { //если "компьютер" не включён, то
				sew.oned = true; //включить его
				sew.bootr = true; //разрешить вывод текста с задержкой
				sel = 0; //начинать заполнять массив
				bboot (sel); //с первой позиции (перезагрузки не было)
				sew.setBackground(Color.BLACK); //установить фон окна чёрным 
				sew.repaint();//(перерисовка окна "монитора")
			} else {
				sew.oned = false; //если "компьютер" включён, то выключить его
				for (int i = 0; i<25; i++) screenst[i] = ""; //очистить массив строк
				sew.setBackground(new Color(245, 245, 245)); //установить светло-серый цвет окна
			}
		if (sew.oned && frw.button2 == but.getSource()) { //если "РС" включён и источником события
			//является Reset кнопка 
			sew.bootr = true; //разрешить вывод текста с задержкой
			sel = 3; //установить смещение в массиве
			screenst[0] = "Restarting..."; 
			screenst[1] = "SEND KillBill to services";
			screenst[2] = "All services was stopted"; //задать первым трём строкам массива значения
			bboot (sel); //дополнить массив оставшимся текстом
			sew.repaint(); //вызвать перерисовку окна
		}
		if (sew.oned && frw.button3 == but.getSource()) { //если "РС" включён и источником события
			//является "eject" кнопка 
			frw.rest = true; //разрешить мигание индикатора на приводе
			frw.repaint();	//вызвать перерисовку первого окна (панель системного блока)
			screenst[sel+17] = "C:\\>Lol. Drive is empty, dupe."; //добавить новое сообщение в массив строк
			sew.repaint(); //вызвать перерисовку второго окна (монитор)
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Virtu_pc(); //вызов конструктора главного класса программы
	}

}
