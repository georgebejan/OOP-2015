package anoyingame.ui;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameFrame extends JFrame implements ActionListener{
	RandomRectangle theRectangle = new RandomRectangle();
	
	QuestionPanel question = new QuestionPanel();
	
	boolean rightAnswer;
	private JButton red = new JButton("RED");
	private JButton blue = new JButton("BLUE");
	private JButton yellow = new JButton("YELLOW");
	private JButton orange = new JButton("ORANGE");
	private JButton green = new JButton("GREEN");
	private JButton pink = new JButton("PINK");
	private JLabel blank = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private JLabel timerLabel = new JLabel();
	private String userAnswer;
	private JPanel answerPanel;
	private Timer timer = null;
	private int count;
	private int mode;
	private int score = 0;
	
	
	public void setCount(int value){
		switch(value){
		case 1: count = 6; break;
		case 2: count = 4; break;
		case 3: count = 2; break;
		}
	}
	public void updateScore(int theScore){
		if(this.playerIsRight()== true)
			score = theScore +1;
			scoreLabel.setText(String.valueOf(score));
	}
	
	public int getScore()
	{
		return score;
	}
	
	public GameFrame(){
		
		answerPanel = new JPanel();
		answerPanel.setLayout(new GridLayout(3,3));
		answerPanel.add(timerLabel);
		answerPanel.add(blank);
		
		answerPanel.add(scoreLabel);
		answerPanel.add(red);
		answerPanel.add(blue);
		answerPanel.add(yellow);
		answerPanel.add(orange);
		answerPanel.add(green);
		answerPanel.add(pink);
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 500);
		this.add(theRectangle,BorderLayout.CENTER);
		this.add(question, BorderLayout.PAGE_START);
		this.add(answerPanel, BorderLayout.PAGE_END);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		scoreLabel.setText("0");
		red.addActionListener(this);
		blue.addActionListener(this);
		yellow.addActionListener(this);
		orange.addActionListener(this);
		green.addActionListener(this);
		pink.addActionListener(this);
		
		}
	
	public void allowTimerStart(boolean permision){
		if(permision == true){
			TimeClass tc = new TimeClass(count);
			timer = new Timer(1000,tc);
			timer.start();
		}
	}
		@Override
		public void actionPerformed(ActionEvent event) {
			userAnswer = event.getActionCommand();
			timerLabel.setText("Time left:" + count);
			
			
			if((event.getSource()==red) || (event.getSource()==blue) || 
					(event.getSource()==yellow) || (event.getSource()==green) || 
					(event.getSource()==orange) || (event.getSource()==pink)) 
					{
						if(playerIsRight()) {
							score+=1;
							scoreLabel.setText(String.valueOf(score));
						}
						timer.restart();
						theRectangle.setRandomText();
						question.setQuestionNumber();
						
					}
			
		}
	
	public class TimeClass implements ActionListener{
		int counter;	
		private int countLimit;
		public TimeClass(int counter){
			this.counter = counter;
			countLimit = counter;
		}
		
		public void actionPerformed(ActionEvent tc){
			counter --;
			if(counter >=1)
				timerLabel.setText("Time left: " + counter);
			else{
				counter = countLimit+1;
				timerLabel.setText("FAIL");
				Toolkit.getDefaultToolkit().beep();
				theRectangle.setRandomText();
				question.setQuestionNumber();
			}
					
			}
		}
	
	
	public String getAnswer(){
		return userAnswer;
	}
	
	public int getQuestion(){
		return question.getQuestionNumber();
	}
		
	public String getCorrectTextColor(){
		return theRectangle.getTextColor();
	}
	
	public String getCorrectRectangleColor(){
		return theRectangle.getRectangleColor();
	}
	
	public String getCorrectText(){
		return theRectangle.getWrittenText();
	}

	public boolean playerIsRight (){
		question.printNumber();
		if(getQuestion()==0)
			if(getCorrectRectangleColor().equals(getAnswer()))
				rightAnswer = true;
			else
				rightAnswer = false;
		else
			if(getQuestion()==1)
				if(getCorrectTextColor().equals(getAnswer()))
					rightAnswer = true;
				else
					rightAnswer = false;
			else
				if(getCorrectText().equals(getAnswer()))
					rightAnswer = true;
				else
					rightAnswer = false;
		return rightAnswer;
	}
}
