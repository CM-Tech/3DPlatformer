package game;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleMeshShape;
import com.bulletphysics.collision.shapes.TriangleShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class PhyObject implements Serializable{
	/*
	 * for now simple collision not proper collision solving just preventing intersection
	 */
	public Vec3 vel=new Vec3(0,0,0);
	public Vec3 fVel=new Vec3(0,0,0);
	public boolean dynamic=false;
	public Vec3 worldPos=new Vec3(0,0,0);
	public Transform trans=new Transform();
	public Vec3 futureWorldPos=new Vec3(0,0,0);
	public MeshCollider collider;
	public CollisionShape mC=new TriangleShape();
	public RigidBody rB;
	public Mesh mesh=new Mesh();
	public float mass=1f;
	public PhyObject(Mesh m,CollisionShape tS,DefaultMotionState state,float mass){
		this.mesh=m.clone();
		Vector3f fallInertia=new Vector3f(0, 0, 0);
        tS.calculateLocalInertia(mass, fallInertia);
        RigidBodyConstructionInfo playerRigidBodyCI=new RigidBodyConstructionInfo(mass, state, tS, fallInertia);
		rB=new RigidBody(playerRigidBodyCI);
		
	}
	public PhyObject(Mesh m,CollisionShape tS,DefaultMotionState state,DynamicsWorld world,float mass){
		this(m, tS, state, mass);
		world.addRigidBody(this.rB);
	}
	/*public PhyObject(Vec3 pos){
		mesh=new Mesh();
		this.collider=new MeshCollider();
		worldPos=pos;
		this.collider.worldPos=this.worldPos;
		this.futureWorldPos=this.worldPos;
	}
	
	public PhyObject(){
		this.collider=new MeshCollider();
		mesh=new Mesh();
	}*/
	public void updatePos(){
		this.collider.worldPos=this.worldPos;
	}
	public Vec3[][] getTris(){
		this.rB.getWorldTransform(this.trans);
		Vec3[][] tranTris= this.mesh.getTris();
		Vec3[][] tranTrisD=new Vec3[tranTris.length][3];
		if(tranTris.length>0){
		for (int i = 0; i < tranTris.length; i++) {
			tranTrisD[i]=new Vec3[3];
			for (int m = 0; m < 3; m++) {
				Vector3f temp=new Vector3f(tranTris[i][m].x,tranTris[i][m].y,tranTris[i][m].z);
				this.trans.transform(temp);
				tranTrisD[i][m]=new Vec3(temp.x,temp.y,temp.z).multiply(100f);//tranTris[i][m].add(this.worldPos);
			
			}
		}
		}
		return tranTrisD;
	}
	public void move(int steps,ArrayList<PhyObject> collisionObjects){
		this.futureWorldPos=this.worldPos.clone();
		this.fVel=this.vel.clone();
		this.collider.worldPos=this.worldPos;
		ArrayList<PhyObject> futureObjects=new ArrayList<PhyObject>();
		boolean downCollide=false;
		for(PhyObject p:collisionObjects){
			if(p!=this){
			futureObjects.add(p);
			futureObjects.get(futureObjects.size()-1).worldPos.translate(p.vel);
			futureObjects.get(futureObjects.size()-1).collider.worldPos=futureObjects.get(futureObjects.size()-1).worldPos;
			}
		}
		for(int i=0;i<futureObjects.size();i++){
			if(futureObjects.get(i).collider.intersects(this.collider)){
				downCollide=true;
			}
		}
		
		if(this.dynamic&&(downCollide==false)){
			
			this.fVel.y+=10f;
		}else{
			//this.futureWorldPos.translate(new Vec3(0,-Math.abs(this.fVel.y)*1.0f,0));
			this.fVel.y-=10f;
		}
	}
	public void applyMove(){
		this.vel=this.fVel.clone().multiply(0.999f);
		this.futureWorldPos.translate(fVel);
		this.worldPos=this.futureWorldPos.clone();
		this.updatePos();
	}
	public void translate(Vec3 v) {
		this.rB.translate(new Vector3f(v.x/100f,v.y/100f,v.z/100f));
		//this.worldPos.translate(vec3);
		//this.updatePos();
		
		// TODO Auto-generated method stub
		
	}
	public void setPos(Vec3 vec3) {
		this.worldPos=(vec3.clone());
		//this.updatePos();
		
		// TODO Auto-generated method stub
		
	}
	/*public PhyObject clone(){
		PhyObject ret=new PhyObject(this.worldPos);
		ret.futureWorldPos=this.futureWorldPos.clone();
		ret.fVel=this.fVel.clone();
		ret.vel=this.vel.clone();
		ret.mesh=this.mesh.clone();
		ret.collider.mesh=this.mesh.clone();
		
		return ret;
	}*/
}
