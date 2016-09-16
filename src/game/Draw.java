package game;
import java.awt.*;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.SimpleDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.RotationalLimitMotor;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

import java.util.*;
import java.util.Timer;
public class Draw implements Serializable{
	
	static final long serialVersionUID = -7298352464830308761L;
	boolean keyDown = false;
	float mul = 1;
	public int score = 0;
	public Vec3 camRot=new Vec3(-45,0,0);
	public Vec3 camPos=new Vec3(0,200,-200);
	public RectangularPrism player=new RectangularPrism(new Vec3(100,100,100));
	
	public Vec3 playerPos=new Vec3(0,0,0);
	public Vec3 playerVel=new Vec3(0,0,0);
	public static int xRot = 0;
	public static int yRot = 0;
	public static int zRot = 0;
	public static String rotMode = "x";
	public BroadphaseInterface broadphase=new DbvtBroadphase();
	public DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
    public CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
    public SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
	public DiscreteDynamicsWorld phyWorld=new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
	public ArrayList<PhyObject> objects = new ArrayList<PhyObject>();
	public DefaultMotionState groundMotionState =
            new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1),new Vector3f(0, -1, 0), 1f)));
	public DefaultMotionState fallMotionState =
            new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 50, 0), 1f)));
	public static int W = 700;
	public static int H = 700;
	public static int camX = W/2;
	public static int camY = H/2;
	public static int x = 0;
	public static int y = 200;
	public static int z = 0;
	public static final Vec3 CENTER = new Vec3(W/2, H/2, 0);
	public FrameDraw panel;
	public JLabel scoreLabel;
	public static JFrame frame;
	public BoxCollider playerBox;
	public StaticPlaneShape groundShape=new StaticPlaneShape(new Vector3f(0,1,0),1);
	public CollisionShape playerShape = new  BoxShape(new Vector3f(1,1,1));
	public RigidBody groundRigidBody;
	public RigidBody playerRigidBody;
	public static boolean jump = false;
	public static boolean grounded = false;
	public static boolean toggled = false;
	static int objNum = 8;
	public static float frameRate = 10;
	public static float rate = frameRate/30;
	static boolean[] keys;
	void load(){
		
		
		
		for(int i = 0; i < 1000; i ++){
			keys = new boolean[i];
		}
		playerBox = new BoxCollider(CENTER.add(new Vec3(0, 50, 0)), CENTER.add(new Vec3(1, 50, 1)));
		frame = new JFrame();
		
		scoreLabel = new JLabel(String.valueOf(score));
		scoreLabel.setSize(new Dimension(100, 100));
		scoreLabel.setHorizontalAlignment((int) JLabel.CENTER_ALIGNMENT);
		scoreLabel.setFont(new Font("Arial", 50, 50));
		scoreLabel.setOpaque(true);
		
		panel = new FrameDraw();
		panel.setSize(new Dimension(W, H));
		
		frame.setPreferredSize(new Dimension(W, H + 50));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(scoreLabel, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		
		(panel).paintComponent(panel.getGraphics());
		
		frame.pack();
		Timer t = new Timer();
		t.schedule(new DoStuff(), 0, (long) frameRate);
	}
	void init(){
		phyWorld.setGravity(new Vector3f(0,-10,0));
		RigidBodyConstructionInfo groundRigidBodyCI=new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
		groundRigidBody = new RigidBody(groundRigidBodyCI);
		//phyWorld.addRigidBody(groundRigidBody);
		
        //Vector3f fallInertia=new Vector3f(0, 0, 0);
        //playerShape.calculateLocalInertia(1f, fallInertia);
        //RigidBodyConstructionInfo playerRigidBodyCI=new RigidBodyConstructionInfo(1f, fallMotionState, playerShape, fallInertia);
		//playerRigidBody=new RigidBody(playerRigidBodyCI);
		//phyWorld.addRigidBody(playerRigidBody);
		//player.dynamic=true;
		playerRigidBody=player.rB;
		player.translate(new Vec3(0,75,0));
		//player.rB.
		//phyWorld.addConstraint(new RotationalLimitMotor());
		phyWorld.addRigidBody(playerRigidBody);
		objects.add(player);
		for(int i=objNum-1; i>=0; i--){
			RectangularPrism p = new RectangularPrism(new Vec3(250, 25, 250));
			p.translate(new Vec3(0, 0,i*500));
			p.rB.setMassProps(0, new Vector3f(0, 0, 0));
			phyWorld.addRigidBody(p.rB);
			//p.translate(new Vec3(0, 0,i*500));
			//p.updatePos();
			objects.add(p);
		}
		load();
	}
	
	@SuppressWarnings("null")
	Draw() {
		init();
	}
	class DoStuff extends TimerTask{
		@Override
		public void run() {
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
			//arrays = sortFaces(arrays);
			
			g.setColor(Color.black);
			for(int j=0; j<objects.size(); j++){
				PhyObject obj = objects.get(j);
				Vec3[][] triArray=obj.mesh.getTris();
				for (int i = 0; i < triArray.length; i++) {
					for (int m = 0; m < 3; m++) {
					Vec3 vec = triArray[i][m];
					//drawPoint(vec, g);
					char[] charAr = {Integer.toString(i).charAt(0)};
					Point rP=getRenderPoint(vec);
					//g.drawChars(charAr, 0, 1, rP.x, rP.y);
					}
				}
			}
			draw3d(arrays, g);
			
			//g.setColor(Color.green);
			//drawPlayerPoint(CENTER.add(new Vec3(0, 190, 0)), g, 50);
		}
		private int[][] getArray(ArrayList<Vec3> vecs, int[] array){
			int[][] rArray = new int[2][array.length];
			
			for(int i=0; i<array.length; i++){
				Vec3 vec = (Vec3)(vecs.get(array[i]));
				int x = (int)vec.x;
				int y = (int)vec.y;
				int z = (int)vec.z;
				Point rP=getRenderPoint(vec);
				rArray[0][i] = rP.x;//getX(x, z);
				rArray[1][i] = rP.y;//getY(y, z);
			}
			return rArray;
		}
		
		private void draw3d(int[][] arrays, Graphics g){
			ArrayList<Vec3[]> allTris=new ArrayList<Vec3[]>();
			for(int i=0; i<objects.size(); i++){
				PhyObject obj = objects.get(i);
				Vec3[][] triArray=obj.getTris();
				allTris.addAll(Arrays.asList(triArray));
				
				
			}
			//objects.get(0).worldPos.print();
			//for(int i=0; i<objects.size(); i++){
				//PhyObject obj = objects.get(i);
				Vec3[][] triArray=allTris.toArray(new Vec3[0][0]);
				triArray=sortFaces(triArray);//sortFaces(obj.mesh.getTris());
				
				for(int j=0; j<triArray.length; j++){
					//int[] aInt = arrays[j];
					//int[][] aaInt = getArray(obj.mesh, aInt);
					g.setColor(Color.red);
					if(true){//Use Surface normal for shading
					Vec3 V=triArray[j][1].subtract(triArray[j][0]);
					Vec3 W=triArray[j][2].subtract(triArray[j][0]);
					Vec3 normal=new Vec3(V.y*W.z-V.z*W.y,V.z*W.x-V.x*W.z,V.x*W.y-V.y*W.x);
					
					normal=normal.multiply(1.0f/normal.len());
					if(triArray[j][0].add(normal).len()<triArray[j][0].subtract(normal).len()){
						normal=normal.multiply(-1.0f);
					}
					Vec3 light=new Vec3(0.5f,1f,1f);
					light=light.multiply(1.0f/light.len());
					
					g.setColor(new Color(Math.abs(normal.x),Math.abs(normal.y),Math.abs(normal.z)));//Color.getHSBColor(0, 1, normal.dot(light)));
					g.setColor(Color.getHSBColor(0, 1, normal.dot(light)));
					}
					//g.setColor(Color.red);
					int[] xS=new int[3];
					int[] yS=new int[3];
					boolean outOfView=false;
					for(int fI=0;fI<3;fI++){
						Point rp=this.getRenderPoint(triArray[j][fI]);
						xS[fI]=rp.x;
						yS[fI]=rp.y;
						Vec3 transformedVec=camTran(triArray[j][fI].clone());
						if(transformedVec.z<0){
							outOfView=true;
						}
					}
					if(!outOfView){
					g.fillPolygon(xS,yS, 3);
					g.setColor(Color.black);
					//g.drawPolygon(xS,yS, 3);
					}
				}
			//}
		}
		private void drawPoint(Vec3 vec, Graphics g){
			Point rP=getRenderPoint(vec);
			g.fillRect(rP.x, rP.y, 5, 5);
		}
		private void drawPlayerPoint(Vec3 vec, Graphics g, int size){
			g.fillRect((int)(vec.x - size/2), (int)(vec.y-size/2), size, size);
		}
		private Point getRenderPoint(Vec3 vec){
			Vec3 transformedVec=camTran(vec.clone());
			
			return new Point(getX(transformedVec.x, transformedVec.z), H-getY(transformedVec.y, transformedVec.z));
		}
		private Vec3 camTran(Vec3 vec){
			Vec3 transformedVec=vec.clone();
			transformedVec=transformedVec.subtract(camPos);
			transformedVec.rotate("x", new Vec3(0, 0,0),camRot.x);
			transformedVec.rotate("y", new Vec3(0, 0,0),camRot.y);
			transformedVec.rotate("z", new Vec3(0, 0,0),camRot.z);
			return transformedVec;
		}
		private int getX(float x, float z){
//			int rInt = (int)(((x-W/2)*MAX_DIST/z)+W/2);
			int average_len = W/2;
			int rInt = (int)(x/z*H/2)+average_len;//(int) (((x-W/2) * ( average_len ) ) / ( z + ( W/2) )) + W/2;

			if(z <= -average_len){
				//rInt = (int) (((x-W/2) * ( average_len ) ) / (150)) + W/2;
			}
			return rInt;
		}
		private int getY(float y, float z){
//			int rInt = (int)(((y-H/2)*MAX_DIST/z)+H/2);
			int average_len = H/2;
			//y+= 175;
			int rInt =(int)(y/z*H/2)+average_len;// (int) (((y-H/2) * ( average_len) ) / ( z+ ( average_len) )) + H/2;
			if(z <= -average_len){
				//rInt=(int) ((y*4)-1450);
			}
			return rInt;
		}
		private Vec3[][] sortFaces(Vec3[][] arrays){
			//PhyObject obj = objects.get(0);
			//Vec3[][] triArray=obj.mesh.getTris();
	
			Float[][] VecsMidZ = new Float[arrays.length][2];
			
			for(int i=0; i<arrays.length; i++){
				Vec3 vec1 = new Vec3(0,0,0);
				vec1.x = ((Vec3)(arrays[i][0])).x;
				vec1.y = ((Vec3)(arrays[i][0])).y;
				vec1.z = ((Vec3)(arrays[i][0])).z;
				Vec3 vec2 = new Vec3(0,0,0);//(Vec3) vecs.get(arrays[i][2]);
				vec2.x = ((Vec3)(arrays[i][1])).x;
				vec2.y = ((Vec3)(arrays[i][1])).y;
				vec2.z = ((Vec3)arrays[i][1]).z;
				Vec3 vec3 = new Vec3(0,0,0);//(Vec3) vecs.get(arrays[i][2]);
				vec2.x = ((Vec3)(arrays[i][2])).x;
				vec2.y = ((Vec3)(arrays[i][2])).y;
				vec2.z = ((Vec3)(arrays[i][2])).z;
				
				
				VecsMidZ[i][0]=camTran(vec1.add(vec2).add(vec3).multiply(0.333333333f)).len();
//				VecsMidZ[i] = ((int) ((Vec3)(vecs.get(iArray[0]))).z);
				VecsMidZ[i][1] = (float) i;
			}
			Arrays.sort(VecsMidZ, new Comparator<Float[]>() {
				@Override
				public int compare(Float[] o1, Float[] o2) {
					if(o1[0] - o2[0]>0){
						return -1;
					}
					if(o1[0] - o2[0]<0){
						return 1;
					}
					return 0;
				}
			});
			//Arrays.sort(VecsMidZ, Collections.reverseOrder());
			@SuppressWarnings("unused")
			
			Vec3[][] temp = new Vec3[arrays.length][3];
			for(int i=0; i<arrays.length; i++){
				for(int j=0; j<3; j++){
					temp[i][j] = arrays[i][j];
				}
			}
//			temp = arrays;
			
			for(int i=0; i<arrays.length; i++){
				int mod = Math.round(VecsMidZ[i][1]);
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
		int ticks = 0;
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			keyDown = true;
			keyNum = e.getKeyCode();
			keys[e.getKeyCode()] = true;//pressed
			if(keys[KeyEvent.VK_SPACE] && /*toggled*/ grounded){
				jump=true;
//				BoxCollider.timer.schedule(new setToggled(), 40);
			}
		}
		class setToggled extends TimerTask{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Draw.toggled = false;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(keys[KeyEvent.VK_W] && keyDown){
				ticks = 5;
			}
			keys[e.getKeyCode()] = false;
			
		}
		public void update(){
			//camRot.z=(float) (Math.sin(System.currentTimeMillis()/1000.0)*45f);
			//phyWorld.setGravity(new Vector3f((float) (-10*Math.sin(Math.toRadians(camRot.z))),(float) (-10*Math.cos(Math.toRadians(camRot.z))),0));
			if(ticks > 0 && mul == 1){
				ticks -= rate;
			}
			panel.requestFocus();
			//Object3d.addVelocityArray(objects, new Vec3(0, 1, 0).multiply(0.6f*rate));//gravity happens to be 0.32 units be second
			ArrayList left = new ArrayList<Object3d>();
			ArrayList right = new ArrayList<Object3d>();
			phyWorld.stepSimulation(1f);
			player.rB.setAngularFactor(0);
			boolean onGround=false;
			for(int i=1;i<objects.size();i++){
				if(objects.get(i)!=player && objects.get(i)!=objects.get(0)){
					if(player.rB.checkCollideWith(objects.get(i).rB) && objects.get(i).rB.isStaticObject()){
						Vector3f velVecTemp=new Vector3f(0,0,0);
						player.rB.getLinearVelocity(velVecTemp);
						//System.out.println("collide");
						if(velVecTemp.y>-10f){
						onGround=true;
						}
					}
				}
				//o.move(1, objects);
			}
			
			
			Transform pTrans=new Transform();
			player.rB.getWorldTransform(pTrans);
			player.worldPos.x=pTrans.origin.x*100f;
			player.worldPos.y=pTrans.origin.y*100f;
			player.worldPos.z=pTrans.origin.z*100f;
			camPos=player.worldPos.clone();
			camPos.translate(new Vec3(0,200,-200));
			for(int i=objects.size()-1; i>=0; i--){
				if(i%2 == 0){
					left.add(objects.get(i));
				}else{
					right.add(objects.get(i));
				}
			}
			//Object3d.setVelocityArrayX(left, 1, 500);
			//Object3d.setVelocityArrayX(right, -1, 500);
			//if(!playerBox.isTouchingArrayGrav(objects)){
				
			//}
			/*for(int i=objects.size()-1; i>=0; i--){
				Object3d obj = objects.get(i);
				if(playerBox.isTouching(obj.boxCollider)){
					if(objects.size() - i+1>score){
						score = objects.size() - i;
						scoreLabel.setText(String.valueOf(score));
					}
					if(score == objects.size()){
						scoreLabel.setText("YOU WIN!!!");
					}
				}
			}*/
			//Object3d.updateArray(objects);
			mul = 1;
			if(keys[KeyEvent.VK_SHIFT] || keys[KeyEvent.VK_W] && ticks > 0 || keys[KeyEvent.VK_R]){
				mul = 2;
			}
			mul*=rate;
			if(keys[KeyEvent.VK_W] && keyDown){
				//playerRigidBody.applyCentralForce(new Vector3f(0,0,0.2f));
				player.rB.applyCentralImpulse(new Vector3f(0,0.01f,0.2f));
				//Object3d.translateArray(objects, Vec3.FORWARD.multiply(10*mul));
				//camPos.z+= 10;
				//playerPos.z += 10;
				//player.vel.z+=1;
			} if(keys[KeyEvent.VK_S] && keyDown){
				player.rB.applyCentralImpulse(new Vector3f(0,0.01f,-0.2f));
				//Object3d.translateArray(objects, Vec3.BACKWARD.multiply(10*mul));
				//camPos.z-= 10;
				//playerPos.z -= 10;
			} if(keys[KeyEvent.VK_A] && keyDown){
				player.rB.applyCentralImpulse(new Vector3f(-0.2f,0.01f,0));
				//Object3d.translateArray(objects, Vec3.LEFT.multiply(10*mul));
				//camPos.x-= 10;
				//playerPos.x-= 10;
			} if(keys[KeyEvent.VK_D] && keyDown){
				player.rB.applyCentralImpulse(new Vector3f(0.2f,0.01f,0));
				//Object3d.translateArray(objects, Vec3.RIGHT.multiply(10*mul));
				//camPos.x+= 10;
				//playerPos.x += 10;
			}
			if((keys[KeyEvent.VK_SPACE] && keyDown) && onGround){
					player.rB.applyCentralImpulse(new Vector3f(0,0.5f,0));
					//Object3d.translateArray(objects, Vec3.RIGHT.multiply(10*mul));
					//camPos.y+= 10;
					//playerPos.y += 10;
					//System.out.println("UP");
			}
			if(keys[KeyEvent.VK_SHIFT] && keyDown){
				//Object3d.translateArray(objects, Vec3.RIGHT.multiply(10*mul));
				//camPos.y-= 10;
				//playerPos.y -= 10;
		}
			 if(keys[KeyEvent.VK_SPACE] && keyDown && /*toggled*/ grounded){
				Vec3 jumpVec = new Vec3(0, 5, 0);
				
				//Object3d.addVelocityArray(objects, jumpVec.multiply(rate));
			}
			 //objects.get(0).setPos(playerPos);
			 //player.setPos(playerPos);
			if(keys[KeyEvent.VK_M]){
				Main.save();
			}
			if(keys[KeyEvent.VK_L]){
				Main.load();
			}
			if(keys[KeyEvent.VK_R]){
				Main.restart();
			}
//			System.out.println(y);
		}
	}
}
