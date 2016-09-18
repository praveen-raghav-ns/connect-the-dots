//connect the dots
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
public class GameNowC extends Applet implements MouseListener,MouseMotionListener,Runnable
{
	Label l;
	int x=650,y=120,n=5,p,q,flag=0,x1,y1,sx=0,sy=0,sb=0,rx,ry,rb,receive_flag=1,m_score=0,o_score=0,xx,yy;
	int xa[][]=new int[n+1][n+1];
	int ya[][]=new int[n+1][n+1];
	int graph_x[][]=new int[n+1][n+1];
	int graph_y[][]=new int[n+1][n+1];
				String topic;
		String score1;
		String score2;
		String result;
		Socket s;
	DataInputStream dis=null;
	DataOutputStream dos=null;
	Thread t;
	boolean signal = false;
	Color ca=new Color(105,105,105);	
	Color cb=new Color(169,169,169);
		Font style1 = new Font("Comic Sans MS",Font.ITALIC,20);
	public void init()
	{ 	
		try
		{
			setFont(style1);
			t = new Thread(this);
			t.start();
		s=new Socket("localhost",3333);
		dis=new DataInputStream(s.getInputStream());		
		dos=new DataOutputStream(s.getOutputStream());
		}catch(Exception ex){System.out.println(ex);}
		l=new Label("click anywhere to view x , y");
		add(l,BorderLayout.EAST);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void run()
	{
		while(true)
		{
			try
			{
			Thread.sleep(500);
			}catch(Exception e){}
			repaint();
		}
	}
	void check_score_x()
	{
		if(graph_y[sx][sy-1]>=1 && graph_y[sx+1][sy-1]>=1 && graph_x[sx][sy-1]>=1 ) //above
		{
		++m_score;
		}
	if(sx!=n && graph_y[sx][sy]>=1 && graph_y[sx+1][sy]>=1 && graph_x[sx][sy+1]>=1 ) //below
		{
		++m_score;				
		}
	}
	void check_score_y()
	{
		if(graph_x[sx][sy]>=1 && graph_x[sx][sy+1]>=1 && graph_y[sx+1][sy]>=1 ) //right
		{
		++m_score;
		}
		if( sy!=n && graph_x[sx-1][sy]>=1 && graph_x[sx-1][sy+1]>=1 && graph_y[sx-1][sy]>=1 )  //left
		{
		++m_score;
		}
	}	
	void send()
	{
		if(!signal)
		{
		repaint();
		try
		{
		dos.writeUTF(""+sx);
		dos.writeUTF(""+sy);
		dos.writeUTF(""+sb);
		dos.writeUTF(""+m_score);
		}catch(Exception es){}
		signal = true;
		}
	}
	void receive()
	{
		if(signal)
		{
		try
		{
		rx=Integer.parseInt(dis.readUTF());
		ry=Integer.parseInt(dis.readUTF());
		rb=Integer.parseInt(dis.readUTF());
		o_score=Integer.parseInt(dis.readUTF());
			if(rb==0)
			{
			graph_x[rx][ry]=2;
			}
			else if(rb==1)
			{	
			graph_y[rx][ry]=2;
			}
		}catch(Exception er){System.out.println(er);}
		signal = false;
		}		
	}
	public void paint(Graphics G)
	{
		G.setColor(ca);
		G.drawOval(330,400,30,30);
		G.fillOval(330,400,30,30);
		G.setColor(cb);
		G.drawOval(330,400,30,30);
		G.fillOval(330,450,30,30);		
		G.setColor(Color.black);
		G.drawRect(60,370,400,150);
		topic="connect the dots";
		score1=" your score       : "+m_score;
		score2=" opponent score : "+o_score;
		G.drawString(score1,90,430);
		G.drawString(score2,90,470);
		G.setColor(Color.black);
		G.drawString(topic,170,200);
		G.drawOval(150,140,200,100);
		
	if(m_score+o_score==16)
	{
		if(m_score==o_score)
		{
			result=" draw match !!! ";
		}
		if(m_score>o_score)
		{
			result=" you win :)";
		}
		if(m_score<o_score)
		{
			result=" you lose :(";
		}
		G.drawString(result,50,50);
	}
//	else
	{
	String score="my score : "+m_score+"opponent score"+o_score;
		int i,j,space=80;
		G.drawRect(x,y,500,500);
		for(i=1;i<=n;i++)
		{
			for(j=1;j<=n;j++)
			{
			p=x+(space*i);
			q=y+(space*j);
			G.setColor(Color.black);
			G.fillOval(p,q,10,10);
			G.drawOval(p,q,10,10);
			G.setColor(Color.green);
			xa[i][j]=p;
			ya[i][j]=q;
				if(i!=n)
				{
			G.setColor(cb);
			G.drawRect(p,q,80,10);
					if(graph_x[i][j]==1)
					{
					G.setColor(ca);
					G.fillRect(xa[i][j]+10,ya[i][j],80-10,10);
					}
					if(graph_x[i][j]==2)
					{
					G.setColor(cb);
					G.fillRect(xa[i][j]+10,ya[i][j],80-10,10);
					}
				}
				if(j!=n)
				{
			G.setColor(cb);
				G.drawRect(p,q,10,80);
					if(graph_y[i][j]==1)
					{
					G.setColor(ca);
					G.fillRect(xa[i][j],ya[i][j]+10,10,80-10);
					}
					if(graph_y[i][j]==2)
					{
					G.setColor(cb);
					G.fillRect(xa[i][j],ya[i][j]+10,10,80-10);
					}
				}
			}
		}
		}
	}
	public void mousePressed(MouseEvent e)
	{
	x1=e.getX();
	y1=e.getY();
	flag=0;
	int i1,j1;
	l.setText("Clicked at "+x1+" "+y1);
	for(i1=1;i1<=n;i1++)
	{
		for(j1=1;j1<=n;j1++)
		{
			if(!signal)
			{
				if(i1!=n && x1>=xa[i1][j1]+10 && x1<=xa[i1][j1]+80-10 && y1>=ya[i1][j1] && y1<=ya[i1][j1]+10 && graph_x[i1][j1]==0)
				{
				graph_x[i1][j1]=1;
				sx=i1;
				sy=j1;
				sb=0;
				flag=1;
				check_score_x();
						send();
				break;
				}
				if(j1!=n && x1>=xa[i1][j1] && x1<=xa[i1][j1]+10 && y1>=ya[i1][j1]+10 && y1<=ya[i1][j1]+80-10 && graph_y[i1][j1]==0)
				{
				graph_y[i1][j1]=1;
				sx=i1;
				sy=j1;
				sb=1;
				flag=1;
				check_score_y();
						send();
				break;
				}
			}
		}
		if(flag==1)
		{
		break;
		}
	}	
	}
	public void mouseMoved(MouseEvent e){}	
	public void mouseDragged(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e)
	{
		receive();
	}
}
/*<applet code="GameNowC.class" width=1500 height=650>
</applet>*/