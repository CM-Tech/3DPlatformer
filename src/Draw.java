import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
public class Draw {
	
	boolean keyDown = false;
	int mul = 1;
	public static int xRot = 0;
	public static int yRot = 0;
	public static int zRot = 0;
	public static String rotMode = "x";
	ArrayList<Object3d> objects = new ArrayList<Object3d>();
	static ArrayList<Vec3> vecs = new ArrayList<Vec3>();
	RectPrism prism;
	RectPrism prism2;
	public static int W = 1000;
	public static int H = 1000;
	public static Vec3 CENTER = new Vec3(W/2, H/2, 0);
	public static FrameDraw panel;
	public static BoxCollider playerBox;
	public static boolean jump = false;
	public static boolean grounded = false;
	static boolean[] keys;
	
	@SuppressWarnings("null")
	Draw() {
		for(int i = 0; i < 1000; i ++){
			keys = new boolean[i];
		}
		vecs = Vec3.getCube(CENTER, 100);
		prism = new RectPrism(CENTER, 250, 10, 250);
		prism2 = new RectPrism(CENTER, 250, 10, 250);
		objects.add(prism);
		objects.add(prism2);
		prism.translate(new Vec3(0, 200, -300));
		prism2.translate(new Vec3(0, 200, 300));
//		playerBox = new BoxCollider(CENTER.add(Vec3.BACKWARD.multiply(50)), CENTER.add(Vec3.BACKWARD.multiply(50)));
		playerBox = new BoxCollider(CENTER.add(new Vec3(0, 50, 0)), CENTER.add(new Vec3(1, 1, 1)));
		JFrame frame = new JFrame();
		
		panel = new FrameDraw();
		panel.setSize(new Dimension(W, H));
		
		frame.setPreferredSize(new Dimension(W, H));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		
		(panel).paintComponent(panel.getGraphics());
		
		frame.pack();
		Timer t = new Timer();
		t.schedule(new DoStuff(), 0, 30);
	}
	class DoStuff extends TimerTask{
		@Override
		public void run() {
			//Vec3.dilateArray(vecs, CENTER, 0.99f);
			(panel).repaint();
		}
	}
	
	@SuppressWarnings("unused")
	private static void sleep(int m){
		try{Thread.sleep(m);}catch(Exception e){}
	}
	
	@SuppressWarnings("serial")
	class FrameDraw extends JPanel implements KeyListener{
		public static final long MAX_DIST = 1l;
		FrameDraw(){
			addKeyListener(this);
		}
		protected void paintComponent(Graphics g) {
			update();
			g.clearRect(0, 0, W*2, H*2);
			int[] a1 = {0,4,5,1};
			int[] a2 = {2,0,1,3};
			int[] a3 = {6,7,5,4};
			int[] a4 = {6,7,3,2};
			int[] a5 = {3,7,5,1};
			int[] a6 = {2,6,4,0};
			int[][] arrays = {a1, a2, a3, a4, a5, a6};
			arrays = sortFaces(arrays);
			
//			for(int[] i:arrays){
//				System.out.print("{");
//				for(int a:i){
//					System.out.print((prism.get(a)).z + ",");
//				}
//				System.out.println("}");
//			}
			
			g.setColor(Color.black);
			for(int j=0; j<objects.size(); j++){
				Object3d obj = objects.get(j);
				for (int i = 0; i < obj.size(); i++) {
					Vec3 vec = (Vec3) obj.get(i);
					vec.print();
					drawPoint(vec, g);
					char[] charAr = {Integer.toString(i).charAt(0)};
					g.drawChars(charAr, 0, 1, getX(vec.x, vec.z), getY(vec.y, vec.z));
				}
			}
			//System.out.println("\n\n");
			draw3d(arrays, g);
			g.setColor(Color.green);
			drawPoint(CENTER.add(Vec3.DOWN.multiply(100)), g, 50);
			//System.out.println(jump);
			//System.out.println(grounded);
		}
		private int[][] getArray(ArrayList<Vec3> vecs, int[] array){
			int[][] rArray = new int[2][array.length];
			
			for(int i=0; i<array.length; i++){
				Vec3 vec = (Vec3)(vecs.get(array[i]));
				int x = (int)vec.x;
				int y = (int)vec.y;
				int z = (int)vec.z;
				rArray[0][i] = getX(x, z);
				rArray[1][i] = getY(y, z);
			}
			return rArray;
		}
		
		private void draw3d(int[][] arrays, Graphics g){
			for(int i=0; i<arrays.length; i++){
				int[] aInt = arrays[i];
				int[][] aaInt = getArray(prism.vecs, aInt);
				g.setColor(Color.red);
				g.fillPolygon(aaInt[0], aaInt[1], aInt.length);
				g.setColor(Color.black);
				g.drawPolygon(aaInt[0], aaInt[1], aInt.length);
			}
		}
		private void drawPoint(Vec3 vec, Graphics g){
			g.fillRect(getX(vec.x, vec.z), getY(vec.y, vec.z), 5, 5);
		}
		private void drawPoint(Vec3 vec, Graphics g, int size){
			g.fillRect(getX(vec.x-size/2, vec.z), getY(vec.y-size/2, vec.z), size, size);
		}
		private int getX(float x, float z){
//			int rInt = (int)(((x-W/2)*MAX_DIST/z)+W/2);
			int average_len = W/2;
			
			int rInt = (int) (((x-W/2) * ( average_len ) ) / ( z + ( average_len) )) + W/2;

			if(z <= -average_len){
				rInt=(int)x;
			}
			return rInt;
		}
		private int getY(float y, float z){
//			int rInt = (int)(((y-H/2)*MAX_DIST/z)+H/2);
			int average_len = H/2;
			int rInt = (int) (((y-H/2) * ( average_len) ) / ( z+ ( average_len) )) + H/2;
			if(z <= -average_len){
				rInt=(int)100000;
			}
			return rInt;
		}
		private int[][] sortFaces(int[][] arrays){

			Integer[] VecsMidZ = new Integer[arrays.length];
			for(int i=0; i<arrays.length; i++){
				Vec3 vec1 = new Vec3(0,0,0);
				vec1.x = ((Vec3)prism.get(arrays[i][0])).x;
				vec1.y = ((Vec3)prism.get(arrays[i][0])).y;
				vec1.z = ((Vec3)prism.get(arrays[i][0])).z;
				Vec3 vec2 = new Vec3(0,0,0);//(Vec3) vecs.get(arrays[i][2]);
				vec2.x = ((Vec3)prism.get(arrays[i][2])).x;
				vec2.y = ((Vec3)prism.get(arrays[i][2])).y;
				vec2.z = ((Vec3)prism.get(arrays[i][2])).z;
				Vec3.midpoint(vec1, vec2);
				
				VecsMidZ[i]=(Integer)(int) vec1.z;
//				VecsMidZ[i] = ((int) ((Vec3)(vecs.get(iArray[0]))).z);
				VecsMidZ[i] *= 10;
				VecsMidZ[i] += i;
			}
			Arrays.sort(VecsMidZ, Collections.reverseOrder());
			@SuppressWarnings("unused")
			int[] sorted = new int[arrays.length];
			int[][] temp = new int[arrays.length][arrays[0].length];
			for(int i=0; i<arrays.length; i++){
				for(int j=0; j<arrays[0].length; j++){
					temp[i][j] = arrays[i][j];
				}
			}
//			temp = arrays;
			
			for(int i=0; i<arrays.length; i++){
				int mod = VecsMidZ[i]%10;
				if (mod<0) mod += 10;
//				System.out.println(mod);
				arrays[i] = temp[mod];
			}
			
			return arrays;
		}
		public void addNotify() {
	        super.addNotify();
	        requestFocus();
	    }
		
		@Override
		public void keyTyped(KeyEvent e) {

		}
		int keyNum;
		
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			keyDown = true;
			keyNum = e.getKeyCode();
			keys[e.getKeyCode()] = true;//pressed
			if(keys[KeyEvent.VK_SPACE] && grounded){
				jump=true;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			keys[e.getKeyCode()] = false;
		}
		public void update(){
			panel.requestFocus();
			Object3d.addVelocityArray(objects, Vec3.UP.multiply(0.32f));//gravity happens to be 0.32 units be second
			playerBox.isTouchingArrayGrav(objects);
			Object3d.updateArray(objects);
			if(keyNum == KeyEvent.VK_SHIFT){
				if(mul == 1){
					mul = 2;
				}else if(mul==2){
					mul = 1;
				}
			}
			if(keys[KeyEvent.VK_W] && keyDown){
				Object3d.translateArray(objects, Vec3.FORWARD.multiply(5*mul));
			}if(keys[KeyEvent.VK_S] && keyDown){
				Object3d.translateArray(objects, Vec3.BACKWARD.multiply(5*mul));
			}if(keys[KeyEvent.VK_A] && keyDown){
				Object3d.translateArray(objects, Vec3.LEFT.multiply(5*mul));
			}if(keys[KeyEvent.VK_D] && keyDown){
				Object3d.translateArray(objects, Vec3.RIGHT.multiply(5*mul));
			}if(keys[KeyEvent.VK_SPACE] && keyDown && grounded){
				Object3d.setVelocityArray(objects, new Vec3(0, 1, 0).multiply(10f));
			}
		}
	}
}
