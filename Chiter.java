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
package chislin;
/* Задача численного решения определенного интеграла несколькими методами.
 * Для каждого метода задана своя подинтегральная функция (см. метод FUNct). 
 */


import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

public class Chiter implements ActionListener {
	MAFace mwi;
	NumberFormat Df;
	
	@SuppressWarnings("serial")
	class MAFace extends Frame {
		Button button1;
		CheckboxGroup cgrp;
		Checkbox checkbox1,checkbox2,checkbox3,checkbox4;
		TextField edit1,edit2,edit3;
		List listbox1;
								
		MAFace() {
			setTitle("Численное интегрирование.");
			setSize(/* width */410,/* height */288);
			setBackground(Color.white);
			setLocation(480, 320);
			setResizable(false);
								
			setLayout(null);
					
			edit1=new TextField("a");
			edit1.setBounds(/* x */20,/* y */45, /* width */50,/* height */25);
			edit2=new TextField("b");
			edit2.setBounds(/* x */80,/* y */45, /* width */50,/* height */25);
			edit3=new TextField("n");
			edit3.setBounds(/* x */195,/* y */45, /* width */50,/* height */25);
			
			cgrp=new CheckboxGroup();
			checkbox1=new Checkbox("левых прямоугольников",cgrp,true);
			checkbox1.setBounds(/* x */20,/* y */98, /* width */170,/* height */25);
			checkbox2=new Checkbox("правых прямоугольников",cgrp,false);
			checkbox2.setBounds(/* x */20,/* y */128, /* width */178,/* height */25);
			checkbox3=new Checkbox("средних прямоугольников",cgrp,false);
			checkbox3.setBounds(/* x */200,/* y */98, /* width */190,/* height */25);
			checkbox4=new Checkbox("трапеций",cgrp,false);
			checkbox4.setBounds(/* x */200,/* y */128, /* width */100,/* height */25);
			
			listbox1=new List(10);
			listbox1.setBounds(/* x */15,/* y */170, /* width */375,/* height */70);
								
			button1=new Button("интегрировать");
			button1.setBounds(/* x */236,/* y */250, /* width */130,/* height */30);
					
			add(edit1);
			add(edit2);
			add(edit3);
			add(checkbox1);
			add(checkbox2);
			add(checkbox3);
			add(checkbox4);
			add(listbox1);
			add(button1);
								
			addWindowListener (new WindowAdapter() {
				public void windowClosing (WindowEvent ev) {
					System.exit(0);
				}
			});
								
			setVisible(true);			
		}
		public void paint (Graphics ren) {
			ren.setColor(Color.BLACK);
			ren.setFont(new Font("TimesNewRoman", Font.PLAIN, 11));
			ren.drawString("пределы интегрирования", 15, 38);
			ren.drawString("число разбиений", 185, 38);
			ren.setFont(new Font("TimesNewRoman", Font.PLAIN, 14));
			ren.drawString("метод", 185, 92);
			ren.drawLine(/* x */10,/* y */96,/* x */395,/* y */96);
			ren.drawLine(/* x */10,/* y */160,/* x */395,/* y */160);
			}
	}
							
	Chiter() {
		Df=NumberFormat.getInstance();
		Df.setMaximumFractionDigits(7);
		mwi=new MAFace();
		mwi.button1.addActionListener(this);		
	}
				
	public void actionPerformed (ActionEvent bt) {
		int i,n;
		double s,a,b,h,x;
		
		try {
			a=Double.parseDouble(mwi.edit1.getText());
			b=Double.parseDouble(mwi.edit2.getText());
			n=Integer.parseInt(mwi.edit3.getText());
			
			if (n > 0) {
				h=(b-a)/n;
			}
			else {
				mwi.listbox1.removeAll();
				mwi.listbox1.add("ОШИБКА ввода! n должно быть больше нуля.");
				
				a=b=h=n=0;
			}
		}
		catch (NumberFormatException err) {
			mwi.listbox1.removeAll();
			mwi.listbox1.add("ОШИБКА ввода! a, b или n не является числом.");
			
			a=b=h=n=0;
		}
		
		s=0;		
		
		if (mwi.cgrp.getSelectedCheckbox().getName()==mwi.checkbox1.getName()){
			for (i=0;i<n;i++) {
				x=a+i*h;
				s=s+FUNct(x,0);
			}
		}
		if (mwi.cgrp.getSelectedCheckbox().getName()==mwi.checkbox2.getName()){
			for (i=1;i<n+1;i++) {
				x=a+i*h;
				s=s+FUNct(x,1);
			}
		}
		if (mwi.cgrp.getSelectedCheckbox().getName()==mwi.checkbox3.getName()){			
			for (i=1;i<n;i++) {
				x=a+i*h;				
				s=s+FUNct(x,2);
			}
			s=s+FUNct(a,2)/2+FUNct(a+n*h,2)/2;
		}
		if (mwi.cgrp.getSelectedCheckbox().getName()==mwi.checkbox4.getName()) {
			s=FUNct(a,3)/2;
			for (i=1;i<n;i++) {
				x=a+i*h;
				s=s+FUNct(x,3);				
			}
			s=s+FUNct(a+n*h,3)/2;			
		}	
		
		
		mwi.listbox1.add("Значение интеграла: "+Df.format(h*s));
		mwi.listbox1.add("при h= "+Df.format(h));
	}
	
	double FUNct (double ix, int fus) {
		double y=0;
		switch (fus) {		
		case 0: y=Math.sqrt(0.6*ix+1.7)/(2.1*ix+Math.sqrt(0.7*Math.pow(ix,2)+1)); break;
		case 1: y=Math.sqrt(0.4*Math.pow(ix,2)+1.5)/(2.5+Math.sqrt(2*ix+0.8)); break;
		case 2: y=Math.sin(0.5*ix+0.4)/(1.2+Math.cos(Math.pow(ix,2)+0.4)); break;
		case 3: y=1/Math.sqrt(2*Math.pow(ix,2)+3); break;
		}
		return y;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new Chiter();
	}

}
